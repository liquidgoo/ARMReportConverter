import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class User {

    @JacksonXmlProperty(localName = "NAME", isAttribute = true)
    public String name;

    @JacksonXmlProperty(localName = "DEPARTMENT", isAttribute = true)
    public String department;

    @JacksonXmlProperty(localName = "PHONE", isAttribute = true)
    public String phone;

    @JacksonXmlProperty(localName = "EMAIL", isAttribute = true)
    public String email;
}
