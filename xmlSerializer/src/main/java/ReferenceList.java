import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ReferenceList {

    @JacksonXmlProperty(localName = "ID_REFLST", isAttribute = true)
    public int id;

    @JacksonXmlProperty(localName = "NAME_REFLST", isAttribute = true)
    public String name;

    @JacksonXmlProperty(localName = "TYPE_REFLST", isAttribute = true)
    public int type; //TODO inheritance?

    @JacksonXmlProperty(localName = "ID_REF", isAttribute = true)
    public int firstReferenceId;


    @JacksonXmlProperty(localName = "REFERENCE_POSITIONS")
    public ListWrapper<StringBlockReference> stringBlockReferences;

    @JacksonXmlProperty(localName = "REFERENCE_UOM")
    public ListWrapper<UnitOfMeasureReference> unitsOfMeasureReferences;
}
