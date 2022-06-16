package marshallingTypes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class UnitOfMeasure {

    @JacksonXmlProperty(localName = "ID_UOM", isAttribute = true)
    public int id;

    @JacksonXmlProperty(localName = "CODE_UOM", isAttribute = true)
    public int code;

    @JacksonXmlProperty(localName = "NAME_UOM", isAttribute = true)
    public String name;

    @JacksonXmlProperty(localName = "MULTIPLIER_UOM", isAttribute = true)
    public double multiplier; //TODO string?

    @JacksonXmlProperty(localName = "NAME_SCALE", isAttribute = true)
    public String scaleName;
}
