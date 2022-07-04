import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;

public class TableSheet extends ExcelSheet {
    private final int FIRST_TABLE_COLUMN_WIDTH_PIXELS = 1;
    private final int DEFAULT_OUTSET_WIDTH = 2;
    private TemplatePart part;
    private final TemplateTable table;
    private final Map<Integer, UnitOfMeasure> unitsOfMeasure;
    private final Map<Integer, ReferenceList> referenceLists;
    private final List<ReportRow> reportRows;
    private final Map<Integer, ExcelRow> rows = new HashMap<>();
    private final List<RowBlock> rowBlocks = new ArrayList<>();
    private final List<RegularRow> emptyRows = new ArrayList<>();
    private final Map<Integer, Integer> graphsColumns = new HashMap<>();
    private int partDescriptionHeight = 0;

    private int tableDescriptionHeight;
    private int maxGraphsLevel;
    private int outsetWidth = DEFAULT_OUTSET_WIDTH;
    private int bottomGraphsCount;
    private int rowsCount = 0;

    private CellStyle tableTopStyle;
    private CellStyle tableCenterStyle;
    private CellStyle deathCellStyle;
    private CellStyle tableCenterNoBordersStyle;
    private CellStyle tableRightStyle;
    private CellStyle tableLeftStyle;

    private boolean hasUOMGraph() {
        return table.isPriorityRow; //TODO ?
    }

    private void calculateGraphsArea() {
        maxGraphsLevel = table.getGraphs().stream().max(Comparator.comparingInt(graph -> graph.level)).get().level;
        bottomGraphsCount = (int) table.getGraphs().stream().filter(graph -> graph.isLeaf).count();
        if (hasUOMGraph()) outsetWidth++;
    }

    private void fillPartDescription() {
        Cell cell = getOrCreateCell(DEFAULT_ROW_OFFSET, DEFAULT_COLUMN_OFFSET);
        String romanNum = RomanNumeralsConverter.arabicToRoman(part.number);
        setCellValue(cell, "РАЗДЕЛ " + romanNum);
        cell.setCellStyle(tableTopStyle);

        cell = getOrCreateCell(DEFAULT_ROW_OFFSET + 1, DEFAULT_COLUMN_OFFSET);
        setCellValue(cell, part.name);
        cell.setCellStyle(tableTopStyle);

        mergeRegion(DEFAULT_ROW_OFFSET, DEFAULT_COLUMN_OFFSET, 1, outsetWidth + bottomGraphsCount);
        mergeRegion(DEFAULT_ROW_OFFSET + 1, DEFAULT_COLUMN_OFFSET, 1, outsetWidth + bottomGraphsCount);

        partDescriptionHeight = 3;
    }

    private void fillTableDescription() {
        int rowOffset = DEFAULT_ROW_OFFSET + partDescriptionHeight;
        int tableWidth = outsetWidth + bottomGraphsCount;

        Cell cell;
        cell = getOrCreateCell(rowOffset, DEFAULT_COLUMN_OFFSET);
        setCellValue(cell, "Таблица " + table.number);
        cell.setCellStyle(tableRightStyle);
        mergeRegion(rowOffset, DEFAULT_COLUMN_OFFSET, 1, tableWidth);
        rowOffset++;

        cell = getOrCreateCell(rowOffset, DEFAULT_COLUMN_OFFSET);
        setCellValue(cell, table.name);
        cell.setCellStyle(tableCenterNoBordersStyle);
        mergeRegion(rowOffset, DEFAULT_COLUMN_OFFSET, 1, tableWidth);
        rowOffset++;

        tableDescriptionHeight = 2;
        if (table.unitOfMeasure != -1) {
            cell = getOrCreateCell(rowOffset, DEFAULT_COLUMN_OFFSET);
            setCellValue(cell, unitsOfMeasure.get(table.unitOfMeasure).name);
            cell.setCellStyle(tableRightStyle);
            mergeRegion(rowOffset, DEFAULT_COLUMN_OFFSET, 1, tableWidth);
            tableDescriptionHeight++;
        }
    }

    private void fillRowsHeader() {
        int rowOffset = DEFAULT_ROW_OFFSET + partDescriptionHeight + tableDescriptionHeight;

        setCellValue(rowOffset, DEFAULT_COLUMN_OFFSET, table.headerName);

        setCellValue(rowOffset, DEFAULT_COLUMN_OFFSET + 1, "Код");
        if (maxGraphsLevel > 1) {
            mergeRegionWidthBorders(rowOffset, DEFAULT_COLUMN_OFFSET, maxGraphsLevel, 1);
            mergeRegionWidthBorders(rowOffset, DEFAULT_COLUMN_OFFSET + 1, maxGraphsLevel, 1);
        }


        setCellValue(rowOffset + maxGraphsLevel, DEFAULT_COLUMN_OFFSET, "А");
        setCellValue(rowOffset + maxGraphsLevel, DEFAULT_COLUMN_OFFSET + 1, "Б");

        if (hasUOMGraph()) {
            setCellValue(rowOffset, DEFAULT_COLUMN_OFFSET + 2, "Ед. изм.");

            if (maxGraphsLevel > 1) {
                mergeRegionWidthBorders(rowOffset, DEFAULT_COLUMN_OFFSET + 2, maxGraphsLevel, 1);

            }
            setCellValue(rowOffset + maxGraphsLevel, DEFAULT_COLUMN_OFFSET + 2, "В");

        }
    }

    private int fillGraphs(int rowOffset, int columnOffset, TreeNode<TemplateGraph> node) {
        UnitOfMeasure uom = unitsOfMeasure.get(node.value.unitOfMeasureId);
        String uomString = uom == null ? "" : (", " + uom.name);

        Cell cell = getOrCreateCell(rowOffset, columnOffset);
        setCellValue(cell, node.value.name + uomString);
        graphsColumns.put(node.value.id, columnOffset);

        int childrenWidth = node.child == null ? 1 : fillGraphs(rowOffset + 1, columnOffset, node.child);

        int extraRows = node.value.isLeaf ? maxGraphsLevel - node.value.level : 0;
        if (childrenWidth > 1 || extraRows > 0) {
            mergeRegionWidthBorders(rowOffset, columnOffset, extraRows + 1, childrenWidth);
        }
        int siblingsWidth = node.sibling == null ?
                0 : fillGraphs(rowOffset, columnOffset + childrenWidth, node.sibling);

        return childrenWidth + siblingsWidth;
    }

    private void fillGraphs() {
        table.updateGraphTree();//TODO
        TreeNode<TemplateGraph> root = table.graphTreeRoot;
        int rowOffset = DEFAULT_ROW_OFFSET + partDescriptionHeight + tableDescriptionHeight;
        int columnOffset = DEFAULT_COLUMN_OFFSET + outsetWidth;

        fillGraphs(rowOffset, columnOffset, root);

        for (Integer graphColumnNumber : graphsColumns.values()) {
            setCellValue(rowOffset + maxGraphsLevel, graphColumnNumber, graphColumnNumber - columnOffset + 1);
        }
    }

    @Override
    protected void setColumnsWidth() {
        setColumnWidth(0, FIRST_TABLE_COLUMN_WIDTH_PIXELS);
        setColumnWidth(DEFAULT_COLUMN_OFFSET, table.nameWidth);
        setColumnWidth(DEFAULT_COLUMN_OFFSET + 1, table.codeWidth);
        if (hasUOMGraph())
            setColumnWidth(DEFAULT_COLUMN_OFFSET + 2, table.unitOfMeasureWidth);

        for (TemplateGraph graph : table.getGraphs()) {
            if (!graph.isLeaf) continue;
            setColumnWidth(graphsColumns.get(graph.id), graph.width);
        }
    }

    private void fillRowNames() {
        int rowOffset = DEFAULT_ROW_OFFSET + partDescriptionHeight + tableDescriptionHeight + maxGraphsLevel + 1;
        for (TemplateRow row : table.getRows().stream().sorted(Comparator.comparingInt(templateRow -> templateRow.number)).toList()) {
            List<ReportRow> reportRows = this.reportRows.stream().filter(reportRow -> reportRow.templateRowId == row.id).toList();

            if (reportRows.isEmpty()) {
                RegularRow regularRow = new RegularRow(rowOffset, row, null);
                emptyRows.add(regularRow);
                rowOffset++;
                rowsCount++;
                continue;
            }

            switch (row.type) {//TODO enum
                case 1 -> {
                    ReportRow reportRow = reportRows.get(0);
                    ExcelRow excelRow = new RegularRow(rowOffset, row, reportRow);
                    rows.put(reportRow.reportRowId, excelRow);
                    rowOffset++;
                    rowsCount++;
                }
                case 2 -> {
                    ReportRow reportRow = reportRows.get(0);
                    ExcelRow excelRow = new FreeRow(rowOffset, row, (FreeBlock) reportRow);
                    rows.put(reportRow.reportRowId, excelRow);
                    rowOffset++;
                    rowsCount++;
                }
                case 3 -> {
                    rowBlocks.add(new RowBlock(rowOffset, row));
                    rowOffset++;
                    rowsCount++;
                    for (ReportRow reportRow : reportRows.stream().sorted(Comparator.comparingInt(repRow -> ((StringBlock) repRow).rowNumber)).toList()) {
                        if (rows.containsKey(reportRow.reportRowId)) continue;

                        RowBlock rowBlock = new RowBlock(rowOffset, (StringBlock) reportRow);

                        rows.put(reportRow.reportRowId, rowBlock);
                        rowOffset += rowBlock.getHeight();
                        rowsCount += rowBlock.getHeight();
                    }
                }
            }
        }
    }

    private void setCellsStyles() {
        setCellsStyle(DEFAULT_ROW_OFFSET + partDescriptionHeight + tableDescriptionHeight, DEFAULT_COLUMN_OFFSET, maxGraphsLevel + 1, 1, tableCenterStyle);
        setCellsStyle(DEFAULT_ROW_OFFSET + partDescriptionHeight + tableDescriptionHeight, DEFAULT_COLUMN_OFFSET + 1, maxGraphsLevel + rowsCount + 1, bottomGraphsCount + outsetWidth - 1, tableCenterStyle);
        setCellsStyle(DEFAULT_ROW_OFFSET + partDescriptionHeight + tableDescriptionHeight + maxGraphsLevel + 1, DEFAULT_COLUMN_OFFSET, rowsCount, 1, tableLeftStyle);
        setDeathCellStyle();
    }

    private void setDeathCellStyle() {
        for (RowBlock row : rowBlocks) {
            if (row.isFullDeathCells()) {
                for (int columnId : graphsColumns.values()) {
                    getOrCreateCell(row.rowNum, columnId).setCellStyle(deathCellStyle);
                }
            }
        }
        for (ExcelRow row : rows.values()) {
            if (!(row instanceof RowBlock rowBlock)) continue;
            if (rowBlock.isFullDeathCells()) {
                for (int columnId : graphsColumns.values()) {
                    getOrCreateCell(rowBlock.rowNum, columnId).setCellStyle(deathCellStyle);
                }
            }
        }
        for (ExcelRow row : emptyRows) {
            for (int columnId : graphsColumns.values()) {
                getOrCreateCell(row.rowNum, columnId).setCellStyle(deathCellStyle);
            }
        }

        for (TemplateDeathCell deathCell : table.getDeathCells()) {
            List<ReportRow> reportRowsList = reportRows.stream().filter(reportRow -> reportRow.templateRowId == deathCell.rowId).toList();
            if (deathCell.referenceListId != -1) {
                reportRowsList = reportRowsList.stream().filter(reportRow -> ((StringBlock) reportRow).referenceId == deathCell.referenceListId).toList();
            }
            if (reportRowsList.isEmpty()) continue;

            getOrCreateCell(rows.get(reportRowsList.get(0).reportRowId).rowNum, graphsColumns.get(deathCell.graphId)).setCellStyle(deathCellStyle);

        }
    }

    @Override
    public void prepareSheet() {
        calculateGraphsArea();
        if (part != null) fillPartDescription();
        fillTableDescription();
        fillRowsHeader();
        fillGraphs();
        setColumnsWidth();
        fillRowNames();
        setCellsStyles();
        updateRowsRegionHeights(new CellRangeAddress(
                        0,DEFAULT_ROW_OFFSET + partDescriptionHeight + tableDescriptionHeight + maxGraphsLevel + rowsCount,
                        0, DEFAULT_COLUMN_OFFSET + outsetWidth + bottomGraphsCount),
                true);
    }

    public void fillCellValue(ReportCell reportCell) {
        ExcelRow excelRow = rows.get(reportCell.rowId);

        excelRow.fillValue(reportCell);
    }

    @Override
    protected void createCellStyles() {
        Workbook workbook = getWorkbook();
        tableCenterNoBordersStyle = workbook.createCellStyle();
        tableCenterNoBordersStyle.setAlignment(HorizontalAlignment.CENTER);
        tableCenterNoBordersStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        tableCenterNoBordersStyle.setFont(tnr10Font);
        tableCenterNoBordersStyle.setWrapText(true);

        tableCenterStyle = workbook.createCellStyle();
        tableCenterStyle.setAlignment(HorizontalAlignment.CENTER);
        tableCenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        tableCenterStyle.setBorderBottom(BorderStyle.THIN);
        tableCenterStyle.setBorderLeft(BorderStyle.THIN);
        tableCenterStyle.setBorderRight(BorderStyle.THIN);
        tableCenterStyle.setBorderTop(BorderStyle.THIN);
        tableCenterStyle.setWrapText(true);
        tableCenterStyle.setFont(tnr10Font);

        deathCellStyle = workbook.createCellStyle();
        deathCellStyle.cloneStyleFrom(tableCenterStyle);
        deathCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        deathCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);

        tableTopStyle = workbook.createCellStyle();
        tableTopStyle.cloneStyleFrom(tableCenterNoBordersStyle);
        tableTopStyle.setVerticalAlignment(VerticalAlignment.TOP);

        tableRightStyle = workbook.createCellStyle();
        tableRightStyle.cloneStyleFrom(tableTopStyle);
        tableRightStyle.setAlignment(HorizontalAlignment.RIGHT);

        tableLeftStyle = workbook.createCellStyle();
        tableLeftStyle.cloneStyleFrom(tableCenterStyle);
        tableLeftStyle.setAlignment(HorizontalAlignment.LEFT);
        tableLeftStyle.setVerticalAlignment(VerticalAlignment.TOP);
    }

    public TableSheet(Sheet sheet, TemplateTable table, List<ReportRow> reportRows
            , Map<Integer, ReferenceList> referenceLists, Map<Integer, UnitOfMeasure> unitsOfMeasure) {
        super(sheet);
        this.table = table;
        this.reportRows = reportRows;
        this.referenceLists = referenceLists;
        this.unitsOfMeasure = unitsOfMeasure;
    }

    public TableSheet(Sheet sheet, TemplatePart part, TemplateTable table, List<ReportRow> reportRows
            , Map<Integer, ReferenceList> referenceLists, Map<Integer, UnitOfMeasure> unitsOfMeasure) {
        super(sheet);
        this.part = part;
        this.table = table;
        this.reportRows = reportRows;
        this.referenceLists = referenceLists;
        this.unitsOfMeasure = unitsOfMeasure;
    }

    public abstract class ExcelRow {
        protected int rowNum;

        public void fillValue(ReportCell reportCell) {
            setCellValue(rowNum, graphsColumns.get(reportCell.graphId), reportCell.value);
        }

        public ExcelRow(int rowNum) {
            this.rowNum = rowNum;

        }
    }

    public class RegularRow extends ExcelRow {
        public RegularRow(int rowNum, TemplateRow row, ReportRow reportRow) {
            super(rowNum);
            setCellValue(rowNum, DEFAULT_COLUMN_OFFSET, row.name);
            setCellValue(rowNum, DEFAULT_COLUMN_OFFSET + 1, row.code);

            if (reportRow == null) return;
            if (hasUOMGraph()) {
                UnitOfMeasure uom = unitsOfMeasure.get(reportRow.unitOfMeasure);
                if (uom != null) {
                    setCellValue(rowNum, DEFAULT_COLUMN_OFFSET + 2, uom.name);
                }
            }
        }

    }

    public class FreeRow extends ExcelRow {
        public FreeRow(int rowNum, TemplateRow row, FreeBlock reportRow) {
            super(rowNum);
            setCellValue(rowNum, DEFAULT_COLUMN_OFFSET, reportRow.rowName);
            setCellValue(rowNum, DEFAULT_COLUMN_OFFSET + 1, row.code);

            if (hasUOMGraph()) {
                UnitOfMeasure uom = unitsOfMeasure.get(reportRow.unitOfMeasure);
                if (uom != null) {
                    setCellValue(rowNum, DEFAULT_COLUMN_OFFSET + 2, uom.name);
                }
            }
        }
    }

    public class RowBlock extends ExcelRow {
        private final Map<Integer, Integer> uomRowNums = new HashMap<>();
        private StringBlockReference reference;
        private Set<Integer> mergedGraphs = new HashSet<>();

        public boolean isFullDeathCells() {
            return reference == null || !(reference.isFullResult || reference.isLeaf); //TODO ???
        }

        public int getHeight() {
            return uomRowNums.isEmpty() ? 1 : uomRowNums.size();
        }

        private void mergeUOM() {
            int height = uomRowNums.size();
            if (height <= 1) return;
            mergeRegionWidthBorders(rowNum, DEFAULT_COLUMN_OFFSET, height, 1);
            mergeRegionWidthBorders(rowNum, DEFAULT_COLUMN_OFFSET + 1, height, 1);

            for (TemplateGraph graph : table.getGraphs()) {
                if (!graph.isLeaf) continue;

                if (graph.unitOfMeasureId != 0) { //TODO 0
                    mergeRegionWidthBorders(rowNum, graphsColumns.get(graph.id), height, 1);
                    mergedGraphs.add(graph.id);
                }
            }
        }

        @Override
        public void fillValue(ReportCell reportCell) {
            int rowNum = this.rowNum;

            if (!uomRowNums.isEmpty() && !mergedGraphs.contains(reportCell.graphId)) {
                rowNum = uomRowNums.get(reportCell.unitOfMeasure);
            }
            setCellValue(rowNum, graphsColumns.get(reportCell.graphId), reportCell.value);
        }

        public RowBlock(int rowNum, StringBlock reportRow) {
            super(rowNum);
            ReferenceList referenceList = referenceLists.get(reportRow.referenceListId);
            reference = referenceList.stringBlockReferences.list.stream().filter(ref -> ref.id == reportRow.referenceId).findAny().get();

            setCellValue(rowNum, DEFAULT_COLUMN_OFFSET, reference.name);
            setCellValue(rowNum, DEFAULT_COLUMN_OFFSET + 1, reference.code);

            if (hasUOMGraph()) {
                List<UnitOfMeasureReference> uomRefs = referenceList.unitsOfMeasureReferences.list.stream().filter(ref -> ref.stringBlockReferenceId == reportRow.referenceId).toList();

                int uomRow = rowNum;
                for (UnitOfMeasureReference uomRef : uomRefs) {
                    UnitOfMeasure uom = unitsOfMeasure.get(uomRef.unitOfMeasureId);
                    setCellValue(uomRow, DEFAULT_COLUMN_OFFSET + 2, uom.name);

                    uomRowNums.put(uom.id, uomRow);
                    uomRow++;


                }
                mergeUOM();
            }
        }

        public RowBlock(int rowNum, TemplateRow row) {
            super(rowNum);
            setCellValue(rowNum, DEFAULT_COLUMN_OFFSET, row.name);
            setCellValue(rowNum, DEFAULT_COLUMN_OFFSET + 1, row.code);
        }
    }
}
