import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class TemplateDeathCell {

    @JacksonXmlProperty(localName = "ID_DROW", isAttribute = true)
    public int rowId;

    @JacksonXmlProperty(localName = "ID_DGP", isAttribute = true)
    public int graphId;

    @JacksonXmlProperty(localName = "ID_REF", isAttribute = true)
    public int referenceListId;

    @JacksonXmlProperty(localName = "ID_DGC", isAttribute = true)
    public double dgcId; //TODO ignore?
}
