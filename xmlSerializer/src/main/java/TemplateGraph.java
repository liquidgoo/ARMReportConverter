import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class TemplateGraph {

    @JacksonXmlProperty(localName = "ID_DGP", isAttribute = true)
    public int id;

    @JacksonXmlProperty(localName = "NUMBER_DGP", isAttribute = true)
    public int number;

    @JacksonXmlProperty(localName = "ID_PARENT_DGP", isAttribute = true)
    public int parentId;

    @JacksonXmlProperty(localName = "NAME_DGP", isAttribute = true)
    public String name;

    @JacksonXmlProperty(localName = "ID_UOM", isAttribute = true)
    public int unitOfMeasureId;

    @JacksonXmlProperty(localName = "FORMAT_UOM", isAttribute = true)
    public String unitOfMeasureFormat;

    @JacksonXmlProperty(localName = "WIDTH_GRAPH", isAttribute = true)
    public int width;

    @JacksonXmlProperty(localName = "ISLEAF", isAttribute = true)
    public Boolean isLeaf;

    @JacksonXmlProperty(localName = "LVL", isAttribute = true)
    public int level;
}
