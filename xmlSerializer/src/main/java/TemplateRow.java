import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class TemplateRow {

    @JacksonXmlProperty(localName = "ID_DROW", isAttribute = true)
    public int id;

    @JacksonXmlProperty(localName = "CODE_DROW", isAttribute = true)
    public String code; //TODO String?

    @JacksonXmlProperty(localName = "NUMBER_DROW", isAttribute = true)
    public int number;

    @JacksonXmlProperty(localName = "NAME_DROW", isAttribute = true)
    public String name;

    @JacksonXmlProperty(localName = "TYPE_ROW", isAttribute = true)
    public int type; //TODO inheritance?

    @JacksonXmlProperty(localName = "ID_REFLST", isAttribute = true)
    public int referenceListId;

    @JacksonXmlProperty(localName = "IS_FILL_FULL_RESULT", isAttribute = true)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    public Boolean isFillFullResult;
}
