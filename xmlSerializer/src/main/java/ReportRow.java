import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonTypeName("1")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "TYPE_ROW")
@JsonSubTypes({@JsonSubTypes.Type(ReportRow.class), @JsonSubTypes.Type(FreeBlock.class), @JsonSubTypes.Type(StringBlock.class)}) //TODO type 4
public class ReportRow {

    @JacksonXmlProperty(localName = "ID_DTABLE", isAttribute = true)
    public int tableId;

    @JacksonXmlProperty(localName = "ID_ROWRPT", isAttribute = true)
    public int reportRowId;

    @JacksonXmlProperty(localName = "ID_DROW", isAttribute = true)
    public int templateRowId;

    @JacksonXmlProperty(localName = "ID_UOM", isAttribute = true)
    public int unitOfMeasure;
}
