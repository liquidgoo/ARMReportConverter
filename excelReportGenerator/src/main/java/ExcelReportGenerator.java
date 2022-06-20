import org.apache.commons.math3.util.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.stream.Stream;

public class ExcelReportGenerator {
    private final int DEFAULT_ROW_OFFSET = 1;
    private final int DEFAULT_COLUMN_OFFSET = 1;
    private final int DEFAULT_PART_DESCRIPTION_HEIGHT = 2;
    private final int DEFAULT_OUTSET_WIDTH = 2;

    private Report report;
    private Template template;

    private void fillTitleSheet(Sheet sheet, int rowOffset, int columnOffset) {
    }

    private void fillPartDescription(Sheet sheet, int rowOffset, int columnOffset, TemplatePart part, int tableWidth) {
        //TODO Roman numerals
        sheet.createRow(rowOffset).createCell(columnOffset).setCellValue("РАЗДЕЛ " + part.number);
        sheet.createRow(rowOffset + 1).createCell(columnOffset).setCellValue(part.name);
        sheet.addMergedRegion(new CellRangeAddress(rowOffset, rowOffset, columnOffset, columnOffset + tableWidth - 1));
        sheet.addMergedRegion(new CellRangeAddress(rowOffset + 1, rowOffset + 1, columnOffset, columnOffset + tableWidth - 1));
    }

    private int fillTableDescription(Sheet sheet, int rowOffset, int columnOffset, TemplateTable table) {
        sheet.createRow(rowOffset).createCell(columnOffset).setCellValue("Таблица " + table.number);
        sheet.createRow(rowOffset + 1).createCell(columnOffset).setCellValue(table.name);

        int tableWidth = getTableWidth(table);

        sheet.addMergedRegion(new CellRangeAddress(rowOffset, rowOffset, columnOffset, columnOffset + tableWidth - 1));
        sheet.addMergedRegion(new CellRangeAddress(rowOffset + 1, rowOffset + 1, columnOffset, columnOffset + tableWidth - 1));

        int descriptionHeight = 2;
        if (table.unitOfMeasure != -1) {
            sheet.createRow(rowOffset + 2).createCell(1).setCellValue(template.getUnitOfMeasure(table.unitOfMeasure).name);
            sheet.addMergedRegion(new CellRangeAddress(rowOffset + 2, rowOffset + 2, columnOffset, columnOffset + tableWidth - 1));
            descriptionHeight++;
        }
        return descriptionHeight;
    }

    private Pair<Integer, Integer> fillGraphs(Sheet sheet, int rowOffset, int columnOffset, TreeNode<TemplateGraph> node, int maxLevel) {

        Pair<Integer, Integer> childrenSize = node.child == null ?
                new Pair<>(1, 0) : fillGraphs(sheet, rowOffset, columnOffset, node.child, maxLevel);


        int childrenWidth = childrenSize.getFirst();
        int childrenHeight = childrenSize.getSecond();

        int rowsLeftAbove = maxLevel - childrenHeight;
        int parentsLeft = node.value.level;

        int rowsForGraph = (int) Math.round((double) rowsLeftAbove / parentsLeft);

        //TODO uom
        int rowNum = rowOffset + maxLevel - childrenHeight - rowsForGraph;
        sheet.getRow(rowNum)
                .createCell(columnOffset).setCellValue(node.value.name);


        if (childrenWidth > 1 || rowsForGraph > 1)
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + rowsForGraph - 1, columnOffset, columnOffset + childrenWidth - 1));

        Pair<Integer, Integer> siblingsSize = node.sibling == null ?
                new Pair<>(0, 0) : fillGraphs(sheet, rowOffset, columnOffset + childrenWidth, node.sibling, maxLevel);


        return new Pair<>(childrenWidth + siblingsSize.getFirst(), Math.max(rowsForGraph + childrenHeight, siblingsSize.getSecond()));
    }

    private void fillRowsHeader(Sheet sheet, int rowOffset, int columnOffset) {
    }

    private void fillRowNames(Sheet sheet, int rowOffset, int columnOffset) {
    }

    private void fillSheetCells(Sheet sheet, int rowOffset, int columnOffset) {
    }

    private void formatDeathCells(Sheet sheet, int rowOffset, int columnOffset) {
    }

    private void fillTable(Sheet sheet, int rowOffset, int columnOffset, TemplateTable table) {
        int descriptionHeight = fillTableDescription(sheet, rowOffset, columnOffset, table);

        int headerHeight = getHeaderHeight(table);
        for (int i = rowOffset + descriptionHeight; i < rowOffset + descriptionHeight + headerHeight; i++) {
            sheet.createRow(i);
        }

        //TODO name, code, uom
        int outsetWidth = DEFAULT_OUTSET_WIDTH;

        fillGraphs(sheet, rowOffset + descriptionHeight, columnOffset + outsetWidth, table.graphTreeRoot, headerHeight);
    }


    private int getTableWidth(TemplateTable table) {
        int graphsWidth = (int) table.getGraphs().stream().filter(graph -> graph.isLeaf).count();
        return DEFAULT_OUTSET_WIDTH + graphsWidth + (table.isPriorityRow ? 1 : 0);
    }

    private int getHeaderHeight(TemplateTable table) {
        return table.getGraphs().stream().max(Comparator.comparingInt(graph -> graph.level)).get().level;
    }


    public void generateExcelReport(OutputStream stream) throws IOException {
        Workbook book = new XSSFWorkbook();


        Sheet titleSheet = book.createSheet("Титульный");
        fillTitleSheet(titleSheet, 1, 1);

        for (TemplatePart part : template.getParts()) {

            TemplateTable firstTable = part.tables.stream().min(Comparator.comparingInt(table -> table.order)).get();
            Sheet firstSheet = book.createSheet("Таблица " + firstTable.number);

            int tableWidth = getTableWidth(firstTable);
            fillPartDescription(firstSheet, DEFAULT_ROW_OFFSET, DEFAULT_COLUMN_OFFSET, part, tableWidth);
            fillTable(firstSheet, DEFAULT_ROW_OFFSET + DEFAULT_PART_DESCRIPTION_HEIGHT + 1, DEFAULT_COLUMN_OFFSET, firstTable);

            Stream<TemplateTable> tables = part.tables.stream().sorted(Comparator.comparingInt(table -> table.order));

            tables.skip(1).forEachOrdered(table -> {
                Sheet sheet = book.createSheet("Таблица " + table.number);
                fillTable(sheet, DEFAULT_ROW_OFFSET, DEFAULT_COLUMN_OFFSET, table);
            });
        }

        //TODO: last sheet

        book.write(stream);
        book.close();
    }

    public ExcelReportGenerator(TemplateRoot templateRoot, ReportRoot reportRoot) {
        template = templateRoot.template;
        report = reportRoot.report;
    }
}