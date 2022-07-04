import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressBase;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.util.Units;

import java.util.*;

public abstract class ExcelSheet {
    protected final int DEFAULT_ROW_OFFSET = 1;
    protected final int DEFAULT_COLUMN_OFFSET = 1;
    protected Font tnr10Font;
    protected Font tnr11Font;
    protected Font tnr12Font;

    private final Sheet sheet;

    protected Row getOrCreateRow(int rowNum) {
        Row row = sheet.getRow(rowNum);
        return row == null ? sheet.createRow(rowNum) : row;
    }

    protected Cell getOrCreateCell(Row row, int columnNum) {
        Cell cell = row.getCell(columnNum);
        return cell == null ? row.createCell(columnNum) : cell;
    }

    protected Cell getOrCreateCell(int rowNum, int columnNum) {
        return getOrCreateCell(getOrCreateRow(rowNum), columnNum);
    }

    protected void setCellValue(Cell cell, String value) {
        cell.setCellValue(value);
    }


    protected void updateRowsRegionHeights(CellRangeAddress range) {
        updateRowsRegionHeights(range, false);
    }


    private int getRegionWidth(CellRangeAddress range) {
        int width = 0;
        for (int i = range.getFirstColumn(); i <= range.getLastColumn(); i++) {
            width += sheet.getColumnWidth(i);
        }
        return width;
    }

    private double getRegionHeight(CellRangeAddress range) {
        double height = 0;
        for (int i = range.getFirstRow(); i <= range.getLastRow(); i++) {
            height += getOrCreateRow(i).getHeightInPoints();
        }
        return height;
    }


    protected void updateRowsRegionHeights(CellRangeAddress range, boolean backwards) {
        Iterator<Integer> rowIterator = backwards ? new ForIterator(range.getLastRow(), range.getFirstRow() - 1, -1)
                : new ForIterator(range.getFirstRow(), range.getLastRow() + 1);
        int i;
        while (rowIterator.hasNext()) {
            i = rowIterator.next();
            Row row = sheet.getRow(i);
            if (row == null) continue;
            for (int j = range.getFirstColumn(); j <= range.getLastColumn(); j++) {
                Cell cell = row.getCell(j);
                if (cell == null) continue;


                boolean isCellMerged = false;
                for (CellRangeAddress mergedRegion : sheet.getMergedRegions()) {
                    if (i < mergedRegion.getFirstRow() || i > mergedRegion.getLastRow()
                            || j < mergedRegion.getFirstColumn() || j > mergedRegion.getLastColumn())
                        continue;
                    isCellMerged = true;
                    int modifyingRow = backwards ? mergedRegion.getFirstRow() : mergedRegion.getLastRow();
                    if (i != modifyingRow) break;

                    double height = 0;

                    Iterator<Integer> mergedRowIterator = backwards ? new ForIterator(mergedRegion.getLastRow()
                            , mergedRegion.getFirstRow(), -1)
                            : new ForIterator(mergedRegion.getFirstRow(), mergedRegion.getLastRow());

                    int k;
                    while (mergedRowIterator.hasNext()) {
                        k = mergedRowIterator.next();
                        height += getOrCreateRow(k).getHeightInPoints();
                    }

                    Cell mergedRegionContentCell = getOrCreateCell(mergedRegion.getFirstRow(), mergedRegion.getFirstColumn());
                    double requiredHeight = HeightUtil.calculateHeight(mergedRegionContentCell
                            .getStringCellValue()
                            , getWorkbook().getFontAt(mergedRegionContentCell.getCellStyle().getFontIndex()).getFontHeightInPoints()
                            , PixelUtil.widthUnits2Pixel(getRegionWidth(mergedRegion)));


                    if (requiredHeight > height + row.getHeightInPoints())
                        row.setHeightInPoints((float) (requiredHeight - height));
                    break;
                }
                if (!isCellMerged) {

                    CellType type = cell.getCellType();
                    String value;
                    if (type == CellType.NUMERIC) {
                        value = Double.toString(cell.getNumericCellValue());
                    } else {
                        value = cell.getStringCellValue();
                    }

                    double height = HeightUtil.calculateHeight(value
                            , getWorkbook().getFontAt(cell.getCellStyle().getFontIndex()).getFontHeightInPoints()
                            , PixelUtil.widthUnits2Pixel(sheet.getColumnWidth(j)));


                    if (height > row.getHeightInPoints())
                        row.setHeightInPoints((float) height);
                }
            }
        }
    }


    protected void setCellValue(Row row, int columnNum, String value) {
        Cell cell = getOrCreateCell(row, columnNum);
        setCellValue(cell, value);
    }

    protected void setCellValue(int rowNum, int columnNum, String value) {
        Cell cell = getOrCreateCell(rowNum, columnNum);
        setCellValue(cell, value);
    }

    protected void setCellValue(Cell cell, int value) {
        Row row = cell.getRow();
        int rowHeight = row.getHeight();
        cell.setCellValue(value);
        int newRowHeight = row.getHeight();
        //rowsHeightManager.updateRowHeight(row.getRowNum(), rowHeight, newRowHeight);
    }

    protected void setCellValue(Row row, int columnNum, int value) {
        Cell cell = getOrCreateCell(row, columnNum);
        setCellValue(cell, value);
    }

    protected void setCellValue(int rowNum, int columnNum, int value) {
        Cell cell = getOrCreateCell(rowNum, columnNum);
        setCellValue(cell, value);
    }

    protected void mergeRegion(CellRangeAddress cellRangeAddress) {
        sheet.addMergedRegion(cellRangeAddress);
        //rowsHeightManager.addMergedRegion(cellRangeAddress);

    }

    protected void mergeRegion(int rowNum, int columnNum, int height, int width) {
        mergeRegion(new CellRangeAddress(rowNum, rowNum + height - 1, columnNum, columnNum + width - 1));
    }


    protected void mergeRegionWidthBorders(int rowNum, int columnNum, int height, int width) {
        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum + height - 1, columnNum, columnNum + width - 1);
        mergeRegion(cellRangeAddress);
        addBordersToMergedRegion(cellRangeAddress);
    }

    protected void setColumnWidth(int columnNum, int widthPixels) {
        sheet.setColumnWidth(columnNum, PixelUtil.pixel2WidthUnits(widthPixels));
        //rowsHeightManager.updateColumnWidth();
    }

    protected abstract void setColumnsWidth();

    protected void addBordersToMergedRegion(CellRangeAddress range) {
        RegionUtil.setBorderBottom(BorderStyle.THIN, range, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, range, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, range, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, range, sheet);
    }

    protected Workbook getWorkbook() {
        return sheet.getWorkbook();
    }

    protected void setCellsStyle(int rowNum, int columnNum, int height, int width, CellStyle cellStyle) {
        for (int i = rowNum; i < rowNum + height; i++) {
            Row row = getOrCreateRow(i);
            for (int j = columnNum; j < columnNum + width; j++) {
                Cell cell = getOrCreateCell(row, j);
                cell.setCellStyle(cellStyle);
            }
        }
    }

    private void setSheetDefaultStyle() {
        sheet.setDisplayGridlines(false);
        sheet.setPrintGridlines(false);
        sheet.setDisplayRowColHeadings(false);
        sheet.setPrintRowAndColumnHeadings(false);
    }

    protected abstract void createCellStyles();

    public abstract void prepareSheet();

    protected void mergeReginWidthBottomBorder(int rowNum, int columnNum, int height, int width) {
        CellRangeAddress range = new CellRangeAddress(rowNum, rowNum + height - 1, columnNum, columnNum + width - 1);
        mergeRegion(range);
        RegionUtil.setBorderBottom(BorderStyle.THIN, range, sheet);
    }

    private void createFonts() {
        Workbook workbook = sheet.getWorkbook();
        tnr10Font = workbook.createFont();
        tnr10Font.setFontName("Times New Roman");
        tnr10Font.setFontHeightInPoints((short) 10);
        tnr11Font = workbook.createFont();
        tnr11Font.setFontName("Times New Roman");
        tnr11Font.setFontHeightInPoints((short) 11);
        tnr12Font = workbook.createFont();
        tnr12Font.setFontName("Times New Roman");
        tnr12Font.setFontHeightInPoints((short) 12);
    }

    public ExcelSheet(Sheet sheet) {
        this.sheet = sheet;
        setSheetDefaultStyle();
        createFonts();
        createCellStyles();
    }
}
