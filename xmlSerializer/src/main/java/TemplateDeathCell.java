import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(value = "ID_DGC")
public class TemplateDeathCell {

    @JacksonXmlProperty(localName = "ID_DROW", isAttribute = true)
    public int rowId;

    @JacksonXmlProperty(localName = "ID_DGP", isAttribute = true)
    public int graphId;

    @JacksonXmlProperty(localName = "ID_REF", isAttribute = true)
    public int referenceListId;
}
