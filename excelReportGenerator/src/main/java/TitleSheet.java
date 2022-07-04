import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Iterator;
import java.util.prefs.PreferenceChangeListener;

public class TitleSheet extends ExcelSheet {
    private TemplateDescription templateDescription;
    private ReportDescription reportDescription;
    private PeriodType periodType;
    private LegalEntityInfo legalEntityInfo;

    private final int TITLE_WIDTH = 15;

    private final int DEFAULT_COLUMN_WIDTH_PIXELS = 67;
    private final int DEFAULT_TITLE_BORDER_COLUMN_PIXELS = 10;

    private final int RESPONDENT_INFO_WIDTH = 8;
    private final int RESPONDENT_TERM_WIDTH = 2;

    private CellStyle titleCenter;
    private CellStyle titleLeft;

    private ReferenceList detailRefList;

    public TitleSheet(Sheet sheet, Template template, Report report, LegalEntityInfo legalEntityInfo) {
        super(sheet);
        this.templateDescription = template.description;
        this.reportDescription = report.description;
        this.periodType = template.periodType;
        this.legalEntityInfo = legalEntityInfo;
    }

    public TitleSheet(Sheet sheet, Template template, Report report, LegalEntityInfo legalEntityInfo, ReferenceList detailRefList) {
        super(sheet);
        this.templateDescription = template.description;
        this.reportDescription = report.description;
        this.periodType = template.periodType;
        this.legalEntityInfo = legalEntityInfo;
        this.detailRefList = detailRefList;
    }

    private String getPeriod() {
        Period period = periodType.periods.stream().filter(x -> x.id == reportDescription.periodId).findAny().get();
        String additionalString = templateDescription.periodType.equals("за период") ? "за " : "";
        return additionalString + period.name;
    }


    private void fillRespondentInfo(int rowOffset, int columnOffset, Respondent respondent) {
        Cell cell;
        Row respondentRow = getOrCreateRow(rowOffset);
        cell = getOrCreateCell(respondentRow, columnOffset);
        setCellValue(cell, respondent.name + "\n\n" + respondent.addressName);
        cell.setCellStyle(titleLeft);
        cell = getOrCreateCell(respondentRow, columnOffset + RESPONDENT_INFO_WIDTH);
        setCellValue(cell, respondent.termName);
        cell.setCellStyle(titleCenter);
    }

    @Override
    protected void setColumnsWidth() {
        for (int i = DEFAULT_COLUMN_OFFSET; i < TITLE_WIDTH + DEFAULT_COLUMN_OFFSET; i++) {
            setColumnWidth(i, DEFAULT_COLUMN_WIDTH_PIXELS);
        }
        setColumnWidth(0, DEFAULT_TITLE_BORDER_COLUMN_PIXELS);
        setColumnWidth(DEFAULT_COLUMN_OFFSET + TITLE_WIDTH, DEFAULT_TITLE_BORDER_COLUMN_PIXELS);
    }

    @Override
    protected void createCellStyles() {
        Workbook workbook = getWorkbook();
        titleCenter = workbook.createCellStyle();
        titleCenter.setAlignment(HorizontalAlignment.CENTER);
        titleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        titleCenter.setFont(tnr11Font);
        titleCenter.setBorderBottom(BorderStyle.THIN);
        titleCenter.setBorderLeft(BorderStyle.THIN);
        titleCenter.setBorderRight(BorderStyle.THIN);
        titleCenter.setBorderTop(BorderStyle.THIN);
        titleCenter.setWrapText(true);


        titleLeft = workbook.createCellStyle();
        titleLeft.cloneStyleFrom(titleCenter);
        titleLeft.setAlignment(HorizontalAlignment.LEFT);
    }

    @Override
    public void prepareSheet() {
        getOrCreateRow(0).setHeight((short) 0);
        int rowOffset = 2;
        int columnOffset = 1;

        setColumnsWidth();

        Cell cell;
        cell = getOrCreateCell(rowOffset, columnOffset);
        setCellValue(cell, "ГОСУДАРСТВЕННАЯ СТАТИСТИЧЕСКАЯ ОТЧЕТНОСТЬ");
        cell.setCellStyle(titleCenter);
        mergeRegionWidthBorders(rowOffset, columnOffset, 1, TITLE_WIDTH);

        rowOffset += 2;

        if (templateDescription.isPrivate) {
            cell = getOrCreateCell(rowOffset, columnOffset);
            setCellValue(cell, "КОНФИДЕНЦИАЛЬНОСТЬ ГАРАНТИРУЕТСЯ ПОЛУЧАТЕЛЕМ ИНФОРМАЦИИ");
            cell.setCellStyle(titleCenter);
            mergeRegionWidthBorders(rowOffset, columnOffset, 1, TITLE_WIDTH);
            rowOffset += 2;
        }

        cell = getOrCreateCell(rowOffset, columnOffset);
        setCellValue(cell, "Представление искаженных данных государственной статистической отчетности, несвоевременное представление или непредставление такой отчетности влекут применение мер административной или уголовной ответственности в соответствии с законодательными актами");
        cell.setCellStyle(titleCenter);
        mergeRegionWidthBorders(rowOffset, columnOffset, 1, TITLE_WIDTH);
        rowOffset += 2;

        int nameOffset = 2;
        cell = getOrCreateCell(rowOffset, columnOffset + nameOffset);
        setCellValue(cell, templateDescription.templateName + " " + getPeriod());
        cell.setCellStyle(titleCenter);
        mergeRegionWidthBorders(rowOffset, columnOffset + nameOffset, 1, TITLE_WIDTH - nameOffset * 2);
        rowOffset += 2;

        int formInfoRowNum = rowOffset;

        Row respondentsRow = getOrCreateRow(rowOffset);
        cell = getOrCreateCell(respondentsRow, columnOffset);
        setCellValue(cell, "Представляют респонденты");
        cell.setCellStyle(titleCenter);
        cell = getOrCreateCell(respondentsRow, columnOffset + RESPONDENT_INFO_WIDTH);
        setCellValue(cell, "Срок представления");
        cell.setCellStyle(titleCenter);
        mergeRegionWidthBorders(rowOffset, columnOffset, 1, RESPONDENT_INFO_WIDTH);
        mergeRegionWidthBorders(rowOffset, columnOffset + RESPONDENT_INFO_WIDTH, 1, RESPONDENT_TERM_WIDTH);
        rowOffset++;


        Iterator<Respondent> respondentIterator = templateDescription.respondents.iterator();
        Respondent firstRespondent = respondentIterator.next();

        fillRespondentInfo(rowOffset, columnOffset, firstRespondent);

        int firstRespondentHeight = 3;
        mergeRegionWidthBorders(rowOffset, columnOffset, firstRespondentHeight, RESPONDENT_INFO_WIDTH);
        mergeRegionWidthBorders(rowOffset, columnOffset + RESPONDENT_INFO_WIDTH, firstRespondentHeight, RESPONDENT_TERM_WIDTH);
        rowOffset += firstRespondentHeight;

        while (respondentIterator.hasNext()) {
            Respondent respondent = respondentIterator.next();

            fillRespondentInfo(rowOffset, columnOffset, respondent);
            rowOffset++;
        }


        int formInfoColumn = columnOffset + RESPONDENT_INFO_WIDTH + RESPONDENT_TERM_WIDTH + 1;
        cell = getOrCreateCell(formInfoRowNum, formInfoColumn);
        setCellValue(cell, "Форма " + templateDescription.formIndex);
        cell.setCellStyle(titleCenter);

        Row formCodeRow = getOrCreateRow(formInfoRowNum + 1);
        cell = getOrCreateCell(formCodeRow, formInfoColumn);
        setCellValue(cell, "Код формы по ОКУД");
        cell.setCellStyle(titleCenter);
        int formCodeColumn = formInfoColumn + 2;
        cell = getOrCreateCell(formCodeRow, formCodeColumn);
        setCellValue(cell, templateDescription.OKUDTemplateCode);
        cell.setCellStyle(titleCenter);
        Row repeatTypeRow = getOrCreateRow(formInfoRowNum + 2);
        cell = getOrCreateCell(repeatTypeRow, formInfoColumn);
        setCellValue(cell, templateDescription.repeatType);
        cell.setCellStyle(titleCenter);

        mergeRegionWidthBorders(formInfoRowNum, formInfoColumn, 1, TITLE_WIDTH + columnOffset - formInfoColumn);
        mergeRegionWidthBorders(formInfoRowNum + 1, formInfoColumn, 1, formCodeColumn - formInfoColumn);
        mergeRegionWidthBorders(formInfoRowNum + 1, formCodeColumn, 1, TITLE_WIDTH + columnOffset - formCodeColumn);
        mergeRegionWidthBorders(formInfoRowNum + 2, formInfoColumn, 1, TITLE_WIDTH + columnOffset - formInfoColumn);

        rowOffset += 2;

        cell = getOrCreateCell(rowOffset, columnOffset);
        setCellValue(cell, "Полное наименование юридического лица: " + legalEntityInfo.getName());
        cell.setCellStyle(titleLeft);
        mergeRegionWidthBorders(rowOffset, columnOffset, 1, TITLE_WIDTH);
        rowOffset++;
        cell = getOrCreateCell(rowOffset, columnOffset);
        setCellValue(cell, "Полное наименование обособленного подразделения юридического лица: " + legalEntityInfo.getSeparateEntityName());
        cell.setCellStyle(titleLeft);
        mergeRegionWidthBorders(rowOffset, columnOffset, 1, TITLE_WIDTH);
        rowOffset++;
        cell = getOrCreateCell(rowOffset, columnOffset);
        setCellValue(cell, "Почтовый адрес (фактический): " + legalEntityInfo.getMailAddress());
        cell.setCellStyle(titleLeft);
        mergeRegionWidthBorders(rowOffset, columnOffset, 1, TITLE_WIDTH);
        rowOffset++;
        cell = getOrCreateCell(rowOffset, columnOffset);
        setCellValue(cell, "Электронный адрес (www, e-mail): " + legalEntityInfo.getEmailAddress());
        cell.setCellStyle(titleLeft);
        mergeRegionWidthBorders(rowOffset, columnOffset, 1, TITLE_WIDTH);
        rowOffset++;

        Row lastPartHeaderRow = getOrCreateRow(rowOffset);
        Row lastPartNumberRow = getOrCreateRow(rowOffset + 1);
        Row lastPartDataRow = getOrCreateRow(rowOffset + 2);

        int ESNCodeWidth = 5;
        cell = getOrCreateCell(lastPartHeaderRow, columnOffset);
        setCellValue(cell, "Регистрационный номер респондента в статистическом регистре (ОКПО)");
        cell.setCellStyle(titleCenter);
        cell = getOrCreateCell(lastPartNumberRow, columnOffset);
        setCellValue(cell, "1");
        cell.setCellStyle(titleCenter);
        cell = getOrCreateCell(lastPartDataRow, columnOffset);
        setCellValue(cell, reportDescription.ESNCode);
        cell.setCellStyle(titleCenter);
        mergeRegionWidthBorders(rowOffset, columnOffset, 1, ESNCodeWidth);
        mergeRegionWidthBorders(rowOffset + 1, 1, columnOffset, ESNCodeWidth);
        mergeRegionWidthBorders(rowOffset + 2, 1, columnOffset, ESNCodeWidth);


        int registrationNumberWidth = 5;
        cell = getOrCreateCell(lastPartHeaderRow, columnOffset + ESNCodeWidth);
        setCellValue(cell, "Учетный номер плательщика (УНП)");
        cell.setCellStyle(titleCenter);
        cell = getOrCreateCell(lastPartNumberRow, columnOffset + ESNCodeWidth);
        setCellValue(cell, "2");
        cell.setCellStyle(titleCenter);
        cell = getOrCreateCell(lastPartDataRow, columnOffset + ESNCodeWidth);
        setCellValue(cell, legalEntityInfo.getPayerRegistrationNumber());
        cell.setCellStyle(titleCenter);
        mergeRegionWidthBorders(rowOffset, columnOffset + ESNCodeWidth, 1, registrationNumberWidth);
        mergeRegionWidthBorders(rowOffset + 1, columnOffset + ESNCodeWidth, 1, registrationNumberWidth);
        mergeRegionWidthBorders(rowOffset + 2, columnOffset + ESNCodeWidth, 1, registrationNumberWidth);

        if (templateDescription.isHaveDetail) {
            int detailWidth = 5;
            cell = getOrCreateCell(lastPartHeaderRow, columnOffset + ESNCodeWidth + registrationNumberWidth);
            setCellValue(cell, templateDescription.detailName);
            cell.setCellStyle(titleCenter);
            cell = getOrCreateCell(lastPartNumberRow, columnOffset + ESNCodeWidth + registrationNumberWidth);
            setCellValue(cell, "3");
            cell.setCellStyle(titleCenter);
            cell = getOrCreateCell(lastPartDataRow, columnOffset + ESNCodeWidth + registrationNumberWidth);

            StringBlockReference blockReference = detailRefList.stringBlockReferences.list.stream().filter(ref -> ref.id == reportDescription.detailId).findAny().get();
            setCellValue(cell, blockReference.name);
            cell.setCellStyle(titleCenter);
            mergeRegionWidthBorders(rowOffset, columnOffset + ESNCodeWidth + registrationNumberWidth, 1, detailWidth);
            mergeRegionWidthBorders(rowOffset + 1, columnOffset + ESNCodeWidth + registrationNumberWidth, 1, detailWidth);
            mergeRegionWidthBorders(rowOffset + 2, columnOffset + ESNCodeWidth + registrationNumberWidth, 1, detailWidth);
        }


        updateRowsRegionHeights(new CellRangeAddress(0, rowOffset + 2, 0, TITLE_WIDTH));
    }
}
