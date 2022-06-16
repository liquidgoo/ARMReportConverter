import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class UnitOfMeasureReference {

    @JacksonXmlProperty(localName = "ID_REF", isAttribute = true)
    public int stringBlockReferenceId;

    @JacksonXmlProperty(localName = "ID_UOM", isAttribute = true)
    public int unitOfMeasureId;

    @JacksonXmlProperty(localName = "FORMAT_UOM", isAttribute = true)
    public String unitOfMeasureFormat;
}
