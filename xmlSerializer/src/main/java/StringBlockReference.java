import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;


public class StringBlockReference {

    @JacksonXmlProperty(localName = "ID_REF", isAttribute = true)
    public int id;

    @JacksonXmlProperty(localName = "CODE_REF", isAttribute = true)
    public String code;

    @JacksonXmlProperty(localName = "ID_PARENT_REF", isAttribute = true)
    public int parentId;

    @JacksonXmlProperty(localName = "NAME_REF", isAttribute = true)
    public String name;

    @JacksonXmlProperty(localName = "IS_FULL_RESULT", isAttribute = true)
    public Boolean isFullResult;

    @JacksonXmlProperty(localName = "ISLEAF", isAttribute = true)
    public Boolean isLeaf; //TODO inheritance?
}
