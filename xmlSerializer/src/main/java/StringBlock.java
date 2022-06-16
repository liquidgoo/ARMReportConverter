import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonTypeName("3")
public class StringBlock extends ReportRow {

    @JacksonXmlProperty(localName = "ID_REF", isAttribute = true)
    public int referenceId;

    @JacksonXmlProperty(localName = "ID_REFLST", isAttribute = true)
    public int referenceListId;

    @JacksonXmlProperty(localName = "NUMBER_ROW", isAttribute = true)
    public int rowNumber;
}
