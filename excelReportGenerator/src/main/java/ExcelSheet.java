import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressBase;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.Units;

import java.util.*;

public abstract class ExcelSheet {
    public class RowsHeightManager {
        private final int EMPTY_ROW_HEIGHT_PIXELS = 19;
        private final double EMPTY_ROW_HEIGHT = Units.pixelToPoints(EMPTY_ROW_HEIGHT_PIXELS);
        private final int ROW_HEIGHT_PIXELS = 17;
        private final double ROW_HEIGHT = Units.pixelToPoints(ROW_HEIGHT_PIXELS);
        private Map<Integer, Double> rowsMinHeights = new HashMap<>();
        private TreeSet<CellRangeAddress> mergedRegions = new TreeSet<>(
                Comparator.comparingInt((CellRangeAddress r) ->
                                r.getFirstRow())
                        .thenComparingInt(CellRangeAddressBase::getLastRow)
                        .thenComparingInt(CellRangeAddress::getFirstColumn)
                        .thenComparingInt(CellRangeAddress::getLastColumn));

        public void addMergedRegion(CellRangeAddress range) {
            mergedRegions.add(range);
            updateRowsHeights(range.getFirstRow());
        }

        public void updateRowHeight(int rowNum, double prevHeight, double height) {
            rowsMinHeights.put(rowNum, height);

            updateRowsHeights(rowNum);
        }

        public void updateColumnWidth() {
            Queue<Integer> rows = new LinkedList<>();
            sheet.rowIterator().forEachRemaining(row -> rows.add(row.getRowNum()));
            updateRowsHeights(rows);
        }

        private void updateRowsHeights(Queue<Integer> affectedRows) {
            Set<CellRangeAddress> handledRegions = new HashSet<>();
            while (!affectedRows.isEmpty()) {
                int changedRow = affectedRows.poll();
                for (CellRangeAddress range : mergedRegions) {
                    if (handledRegions.contains(range)) continue;
                    if (range.getFirstRow() <= changedRow && range.getLastRow() >= changedRow) {
                        int width = 0;
                        for (int i = range.getFirstColumn(); i <= range.getLastColumn(); i++) {
                            width += sheet.getColumnWidth(i);
                        }

                        int mergedHeightPixels = calculateRowHeight(getOrCreateCell(range.getFirstRow(), range.getFirstColumn())
                                .getStringCellValue(), width) * ROW_HEIGHT_PIXELS;
                        double mergedHeight = Units.pixelToPoints(mergedHeightPixels);

                        double currentHeight = 0;
                        for (int i = range.getFirstRow(); i <= range.getLastRow(); i++) {
                            if (rowsMinHeights.containsKey(i)) {
                                currentHeight += rowsMinHeights.get(i);
                            } else {
                                currentHeight += getOrCreateRow(i).getHeightInPoints();
                            }
                        }

                        if (currentHeight < mergedHeight) {
                            double newHeight = mergedHeight / (range.getLastRow()- range.getFirstRow() + 1);
                            handledRegions.add(range);
                            for (int i = range.getFirstRow(); i <= range.getLastRow(); i++) {
                                getOrCreateRow(i).setHeightInPoints((float) newHeight);
                            }

                        }
                    }
                }
            }
        }

        private void updateRowsHeights(int rowNum) {
            Queue<Integer> affectedRows = new LinkedList<>();
            affectedRows.add(rowNum);

            updateRowsHeights(affectedRows);
        }

        private int calculateRowHeight(String text, int columnWidth) {
            int colwidthinchars = columnWidth / 256;
            colwidthinchars = Math.round(colwidthinchars * 4f / 5f);

            String[] chars = text.split("");
            int neededrows = 1;
            int counter = 0;
            for (int i = 0; i < chars.length; i++) {
                counter++;
                if (counter == colwidthinchars) {
                    neededrows++;
                    counter = 0;
                } else if ("\n".equals(chars[i])) {
                    neededrows++;
                    counter = 0;
                }
            }
            return neededrows;
        }
    }

    protected final int DEFAULT_ROW_OFFSET = 1;
    protected final int DEFAULT_COLUMN_OFFSET = 1;
    protected Font tnr10Font;
    protected Font tnr11Font;
    protected Font tnr12Font;

    private Sheet sheet;
    private final RowsHeightManager rowsHeightManager = new RowsHeightManager();

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
        Row row = cell.getRow();
        double rowHeight = row.getHeightInPoints();
        cell.setCellValue(value);
        double newRowHeight = row.getHeightInPoints();
        rowsHeightManager.updateRowHeight(row.getRowNum(), rowHeight, newRowHeight);
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
        rowsHeightManager.updateRowHeight(row.getRowNum(), rowHeight, newRowHeight);
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
        rowsHeightManager.addMergedRegion(cellRangeAddress);

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
        rowsHeightManager.updateColumnWidth();
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
