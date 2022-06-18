import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
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
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    public Boolean isPriorityRow;


    @JacksonXmlProperty(localName = "GRAPH_PROGRAPH")
    private ListWrapper<TemplateGraph> graphs = new ListWrapper<>();

    public List<TemplateGraph> getGraphs() {
        return graphs.list;
    }

    public void setGraphs(List<TemplateGraph> graphs) {
        this.graphs.list = graphs;
    }

    @JacksonXmlProperty(localName = "ROW_IN_TABLE")
    private ListWrapper<TemplateRow> rows = new ListWrapper<>();

    public List<TemplateRow> getRows() {
        return rows.list;
    }

    public void setRows(List<TemplateRow> rows) {
        this.rows.list = rows;
    }

    @JacksonXmlProperty(localName = "DEATH_GRAPH_CELL")
    private ListWrapper<TemplateDeathCell> deathCells = new ListWrapper<>();

    public List<TemplateDeathCell> getDeathCells() {
        return deathCells.list;
    }

    public void setDeathCells(List<TemplateDeathCell> deathCells) {
        this.deathCells.list = deathCells;
    }
}
