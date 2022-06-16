package marshallingTypes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class TemplatePart {

    @JacksonXmlProperty(localName = "ID_DPART", isAttribute = true)
    public int id;

    @JacksonXmlProperty(localName = "NUMBER_DPART", isAttribute = true)
    public int number;

    @JacksonXmlProperty(localName = "NAME_DPART", isAttribute = true)
    public String name;


    @JacksonXmlElementWrapper(localName = "TABLES")
    @JacksonXmlProperty(localName = "row")
    public List<TemplateTable> tables;
}
