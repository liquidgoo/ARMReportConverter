import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "TEMPLATE_ROOT")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateRoot {
    @JacksonXmlProperty(localName = "TEMPLATE_GSN")
    public Template template;
}
