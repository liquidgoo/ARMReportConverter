import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Report {

    @JacksonXmlProperty(localName = "DESCRIPTION")
    public ReportDescription description;

    @JacksonXmlProperty(localName = "ROW_REPORT")
    private ListWrapper<ReportRow> rows = new ListWrapper<>();
    public List<ReportRow> getRows() {
        return rows.list;
    }
    public void setRows(List<ReportRow> rows) {
        this.rows.list = rows;
    }

    @JacksonXmlProperty(localName = "GRAPH_CELL")
    private ListWrapper<ReportCell> cells = new ListWrapper<>();
    public List<ReportCell> getCells() {
        return cells.list;
    }

    public void setCells(List<ReportCell> cells) {
        this.cells.list = cells;
    }

    public int getTemplateRowId(int reportRowId) {
        for (ReportRow row : rows.list) {
            if (reportRowId == row.reportRowId) {
                return row.templateRowId;
            }
        }
        return -1;
    }
}
