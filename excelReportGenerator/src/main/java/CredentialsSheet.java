import org.apache.poi.ss.usermodel.*;

public class CredentialsSheet extends ExcelSheet {
    private SupervisorCredentials supervisorCredentials;
    private User user;
    private final int TOTAL_WIDTH = 15;

    private final int FIRST_CREDENTIALS_COLUMN_PIXELS = 2;

    private final int DEFAULT_CREDENTIALS_COLUMN_PIXELS = 65;
    private final int DEFAULT_COLUMN_OFFSET = 1;

    private final int CREDENTIALS_INFO_WIDTH = 4;
    private final int POSITION_WIDTH = 4;
    private final int INITIALS_WIDTH = 4;
    private final int CREDENTIALS_WIDTH = 8;
    private final int DATE_WIDTH = 5;


    private CellStyle signatureTopStyle;
    private CellStyle signatureBottomStyle;


    @Override
    protected void setColumnsWidth() {
        setColumnWidth(0, FIRST_CREDENTIALS_COLUMN_PIXELS);

        for (int i = DEFAULT_COLUMN_OFFSET; i < TOTAL_WIDTH + DEFAULT_COLUMN_OFFSET; i++) {
            setColumnWidth(i, DEFAULT_CREDENTIALS_COLUMN_PIXELS);
        }
    }

    @Override
    protected void createCellStyles() {
        Workbook workbook = getWorkbook();
        signatureTopStyle = workbook.createCellStyle();
        signatureTopStyle.setAlignment(HorizontalAlignment.CENTER);
        signatureTopStyle.setVerticalAlignment(VerticalAlignment.TOP);
        signatureTopStyle.setFont(tnr10Font);
        signatureTopStyle.setWrapText(true);

        signatureBottomStyle = workbook.createCellStyle();
        signatureBottomStyle.cloneStyleFrom(signatureTopStyle);
        signatureBottomStyle.setFont(tnr12Font);
        signatureBottomStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
        signatureBottomStyle.setBorderBottom(BorderStyle.THIN);
    }

    @Override
    public void prepareSheet() {
        int rowOffset = 0;
        int columnOffset = DEFAULT_COLUMN_OFFSET;

        setColumnsWidth();


        Cell cell;

        Row positionSignatureInitialsRow = getOrCreateRow(rowOffset);
        Row positionSignatureInitialsStringRow = getOrCreateRow(rowOffset + 1);
        cell = getOrCreateCell(positionSignatureInitialsRow, columnOffset);
        setCellValue(cell, "Руководитель респондента или уполномоченный на составление и представление первичных статистических данных работник респондента");
        cell.setCellStyle(signatureTopStyle);
        mergeRegion(rowOffset, columnOffset, 2, CREDENTIALS_INFO_WIDTH);
        columnOffset += CREDENTIALS_INFO_WIDTH + 1;


        cell = getOrCreateCell(positionSignatureInitialsRow, columnOffset);
        setCellValue(cell, supervisorCredentials.getTitle());
        cell.setCellStyle(signatureBottomStyle);
        cell = getOrCreateCell(positionSignatureInitialsStringRow, columnOffset);
        setCellValue(cell, "(должность)");
        cell.setCellStyle(signatureTopStyle);


        mergeReginWidthBottomBorder(rowOffset, columnOffset, 1, POSITION_WIDTH);
        mergeRegion(rowOffset + 1, columnOffset, 1, POSITION_WIDTH);
        columnOffset += POSITION_WIDTH + 1;

        cell = getOrCreateCell(positionSignatureInitialsRow, columnOffset);
        cell.setCellStyle(signatureBottomStyle);
        cell = getOrCreateCell(positionSignatureInitialsStringRow, columnOffset);
        setCellValue(cell, "(подпись)");
        cell.setCellStyle(signatureTopStyle);
        columnOffset += 2;

        cell = getOrCreateCell(positionSignatureInitialsRow, columnOffset);
        setCellValue(cell, supervisorCredentials.getInitials());
        cell.setCellStyle(signatureBottomStyle);
        cell = getOrCreateCell(positionSignatureInitialsStringRow, columnOffset);
        setCellValue(cell, "(инициалы, фамилия)");
        cell.setCellStyle(signatureTopStyle);
        mergeReginWidthBottomBorder(rowOffset, columnOffset, 1, INITIALS_WIDTH);
        mergeRegion(rowOffset + 1, columnOffset, 1, INITIALS_WIDTH);

        columnOffset = DEFAULT_COLUMN_OFFSET;
        rowOffset += 3;

        Row contactInfoDateRow = getOrCreateRow(rowOffset);
        Row contactInfoDateStringRow = getOrCreateRow(rowOffset + 1);

        String contactInfo = supervisorCredentials.getContactInfo();
        if (contactInfo == null) contactInfo = user.phone + ", " + user.email;
        cell = getOrCreateCell(contactInfoDateRow, columnOffset);
        setCellValue(cell, contactInfo);
        cell.setCellStyle(signatureBottomStyle);
        cell = getOrCreateCell(contactInfoDateStringRow, columnOffset);
        setCellValue(cell, "(контактный номер телефона, адрес электронной почты)");
        cell.setCellStyle(signatureTopStyle);
        mergeReginWidthBottomBorder(rowOffset, columnOffset, 1, CREDENTIALS_WIDTH);
        mergeRegion(rowOffset + 1, columnOffset, 1, CREDENTIALS_WIDTH);

        columnOffset = TOTAL_WIDTH - DATE_WIDTH + columnOffset + 1;
        cell = getOrCreateCell(contactInfoDateRow, columnOffset);
        setCellValue(cell, supervisorCredentials.getDate());
        cell.setCellStyle(signatureBottomStyle);
        cell = getOrCreateCell(contactInfoDateStringRow, columnOffset);
        setCellValue(cell, "(дата составления государственной статистической отчетности)");
        cell.setCellStyle(signatureTopStyle);
        mergeReginWidthBottomBorder(rowOffset, columnOffset, 1, DATE_WIDTH);
        mergeRegion(rowOffset + 1, columnOffset, 1, DATE_WIDTH);
    }

    public CredentialsSheet(Sheet sheet, SupervisorCredentials supervisorCredentials, User user) {
        super(sheet);
        this.supervisorCredentials = supervisorCredentials;
        this.user = user;
    }
}
