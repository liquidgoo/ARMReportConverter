import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "REPORT_ROOT")
public class ReportRoot {
    @JacksonXmlProperty(localName = "REPORT")
    public Report report;
}
