package marshallingTypes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;


public class Report {

    @JacksonXmlProperty(localName = "DESCRIPTION")
    public ReportDescription description;

    @JacksonXmlProperty(localName = "ROW_REPORT")
    public ListWrapper<ReportRow> _rows;

    @JacksonXmlProperty(localName = "GRAPH_CELL")
    public ListWrapper<ReportCell> cells;
}
