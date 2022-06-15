package marshallingTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ReportDescription {

    @JacksonXmlProperty(localName = "ID_FT", isAttribute = true)
    public int templateId;

    @JacksonXmlProperty(localName = "ID_P", isAttribute = true)
    public int periodId;

    @JacksonXmlProperty(localName = "CODE_ESN", isAttribute = true)
    public String ESNCode;

    @JacksonXmlProperty(localName = "NAME_ESN", isAttribute = true)
    public String ESNName;

    //TODO multiple users?
    @JacksonXmlProperty(localName = "USERS")
    public User user;
}
