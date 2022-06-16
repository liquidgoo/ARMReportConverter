import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReportGenerator {

    //TODO: replace maps with classes?
    private String getID_ROWRPT(String ID_DROW, XmlReportData reportData) {
        for (Map<String, String> row : reportData.rows) {
            if (row.get("ID_DROW").equals(ID_DROW)) return row.get("ID_ROWRPT");
        }
        return null;
    }

    //TODO: graphs order
    private Map<String, Integer> getGraphsIndexes(String ID_DTABLE, List<Map<String, String>> graphs) {
        int row_num = 2;
        Map<String, Integer> graphInds = new HashMap<>();
        for (Map<String, String> graph : graphs) {
            if (!graph.get("ID_DTABLE").equals(ID_DTABLE) || graph.get("ISLEAF").equals("0")) continue;

            graphInds.put(graph.get("ID_DGP"), row_num);
            row_num++;
        }
        return graphInds;
    }

    public void generateExcelReport(XmlReportData reportData, XmlTemplateData templateData, OutputStream stream) throws IOException {
        Workbook book = new XSSFWorkbook();


        Sheet titleSheet = book.createSheet("Титульный");
        //TODO: title sheet




        templateData.rows.sort(Comparator.comparing(row -> Integer.parseInt(row.get("NUMBER_DROW"))));

        //TODO: formatting
        int sheetNum = 1;
        for (Map<String, String> part : templateData.parts) {
            for (Map<String, String> table : templateData.tables) {
                if (!table.get("ID_DPART").equals(part.get("ID_DPART"))) continue;

                Sheet sheet = book.createSheet("Таблица" + sheetNum);


                int rowNum = 0;
                Map<String, Integer> graphIndexes = getGraphsIndexes(table.get("ID_DTABLE"), templateData.graphs);

                for (Map<String, String> row : templateData.rows) {
                    if (!row.get("ID_DTABLE").equals(table.get("ID_DTABLE"))) continue;

                    Row sheetRow = sheet.createRow(rowNum);

                    Cell titleCell = sheetRow.createCell(0);
                    titleCell.setCellValue(row.get("NAME_DROW"));
                    Cell codeCell = sheetRow.createCell(1);
                    codeCell.setCellValue(row.get("CODE_DROW"));


                    String id_rowrpt = getID_ROWRPT(row.get("ID_DROW"), reportData);


                    for (Map<String, String> cell : reportData.cells) {
                        if (!cell.get("ID_ROWRPT").equals(id_rowrpt)) continue;

                        Cell valueCell = sheetRow.createCell(graphIndexes.get(cell.get("ID_DGP")));
                        valueCell.setCellValue(cell.get("VALUE_GCELL"));
                    }
                    rowNum++;
                }
            }
        }

        //TODO: last sheet

        book.write(stream);
        book.close();
    }
}