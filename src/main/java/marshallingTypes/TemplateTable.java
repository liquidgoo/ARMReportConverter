package marshallingTypes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;


public class TemplateTable {

    @JacksonXmlProperty(localName = "ID_DTABLE", isAttribute = true)
    public int id;

    @JacksonXmlProperty(localName = "NUMBER_DTABLE", isAttribute = true)
    public int number;

    @JacksonXmlProperty(localName = "ORDER_DTABLE", isAttribute = true)
    public int order;

    @JacksonXmlProperty(localName = "NAME_DTABLE", isAttribute = true)
    public String name;

    @JacksonXmlProperty(localName = "ID_UOM", isAttribute = true)
    public int unitOfMeasure;

    @JacksonXmlProperty(localName = "FORMAT_UOM", isAttribute = true)
    public String unitOfMeasureFormat;

    @JacksonXmlProperty(localName = "NAME_HEADER_GRAPH", isAttribute = true)
    public String headerName;

    @JacksonXmlProperty(localName = "HEIGHT_HEADER", isAttribute = true)
    public int headerHeight;

    @JacksonXmlProperty(localName = "WIDTH_COL_NAME", isAttribute = true)
    public int nameWidth;

    @JacksonXmlProperty(localName = "WIDTH_COL_CODE", isAttribute = true)
    public int codeWidth;

    @JacksonXmlProperty(localName = "WIDTH_COL_UOM", isAttribute = true)
    public int unitOfMeasureWidth;

    @JacksonXmlProperty(localName = "IS_PRIORITY_ROW", isAttribute = true)
    public Boolean isPriorityRow;


    @JacksonXmlProperty(localName = "GRAPH_PROGRAPH")
    public ListWrapper<TemplateGraph> graphs;

    @JacksonXmlProperty(localName = "ROW_IN_TABLE")
    public ListWrapper<TemplateRow> rows;

    @JacksonXmlProperty(localName = "DEATH_GRAPH_CELL")
    public ListWrapper<TemplateDeathCell> deathCells;
}
