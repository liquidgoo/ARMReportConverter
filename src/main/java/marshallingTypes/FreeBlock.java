package marshallingTypes;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonTypeName("2")
public class FreeBlock extends ReportRow {

    @JacksonXmlProperty(localName = "VALUE_FREE_ROW", isAttribute = true)
    public String rowName;
}
