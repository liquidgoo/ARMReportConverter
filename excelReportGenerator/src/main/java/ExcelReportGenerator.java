import org.apache.commons.math3.util.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Stream;

public class ExcelReportGenerator {

    private Report report;
    private Template template;

    private LegalEntityInfo legalEntityInfo;
    private SupervisorCredentials supervisorCredentials;

    private final Map<Integer, TableSheet> tableSheets = new HashMap<>();
    private final List<ExcelSheet> sheets = new ArrayList<>();


    private void fillSheetCells() {
        for (ReportCell reportCell : report.getCells()) {
            ReportRow reportRow = report.getRows().stream().filter(row -> row.reportRowId == reportCell.rowId).findAny().get();
            tableSheets.get(reportRow.tableId).fillCellValue(reportCell);
        }
    }


    public void generateExcelReport(OutputStream stream) throws IOException {
        Workbook book = new XSSFWorkbook();

        TitleSheet titleSheet = new TitleSheet(book.createSheet("Титульный"), template, report, legalEntityInfo);
        sheets.add(titleSheet);

        Map<Integer, UnitOfMeasure> unitOfMeasureMap = template.getUnitsOfMeasureAsMap();
        Map<Integer, ReferenceList> referenceListMap = template.getReferenceListsAsMap();
        for (TemplatePart part : template.getParts()) {

            TemplateTable firstTable = part.tables.stream().min(Comparator.comparingInt(table -> table.order)).get();
            Sheet firstSheet = book.createSheet("Таблица " + firstTable.number);
            TableSheet partSheet = new TableSheet(firstSheet, part, firstTable, report.getRowsByTableId(firstTable.id)
                    , referenceListMap, unitOfMeasureMap);

            tableSheets.put(firstTable.id, partSheet);
            sheets.add(partSheet);


            Stream<TemplateTable> tables = part.tables.stream().sorted(Comparator.comparingInt(table -> table.order));

            tables.skip(1).forEachOrdered(table -> {
                Sheet sheet = book.createSheet("Таблица " + table.number);
                TableSheet tableSheet = new TableSheet(sheet, table, report.getRowsByTableId(firstTable.id)
                        , referenceListMap, unitOfMeasureMap);
                tableSheets.put(table.id, tableSheet);
                sheets.add(tableSheet);
            });
        }

        Sheet credentialsSheet = book.createSheet("Подпись");
        CredentialsSheet credentials = new CredentialsSheet(credentialsSheet, supervisorCredentials, report.description.user);
        sheets.add(credentials);

        for (ExcelSheet sheet : sheets) {
            sheet.prepareSheet();
        }

        fillSheetCells();

        book.write(stream);
        book.close();
    }


    public ExcelReportGenerator(TemplateRoot templateRoot, ReportRoot reportRoot, LegalEntityInfo legalEntityInfo, SupervisorCredentials supervisorCredentials) {
        template = templateRoot.template;
        report = reportRoot.report;
        this.legalEntityInfo = legalEntityInfo;
        this.supervisorCredentials = supervisorCredentials;
    }
}