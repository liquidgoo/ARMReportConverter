import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class PeriodType {

    @JacksonXmlProperty(localName = "ID_PT", isAttribute = true)
    public int id;

    @JacksonXmlProperty(localName = "NAME_PT", isAttribute = true)
    public String name;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "PERIOD")
    public List<Period> periods;
}
