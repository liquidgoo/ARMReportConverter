package marshallingTypes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Respondent {

    @JacksonXmlProperty(localName = "RESP_NAME", isAttribute = true)
    public String name;

    @JacksonXmlProperty(localName = "ADDRESS_NAME", isAttribute = true)
    public String addressName;

    @JacksonXmlProperty(localName = "TERM_NAME", isAttribute = true)
    public String termName;
}
