import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Date;

public class Period {

    @JacksonXmlProperty(localName = "ID_P", isAttribute = true)
    public int id;

    @JacksonXmlProperty(localName = "NAME_P", isAttribute = true)
    public String name;

    @JacksonXmlProperty(localName = "BEGINDATE_P", isAttribute = true)
    public Date beginDate;

    @JacksonXmlProperty(localName = "FINALDATE_P", isAttribute = true)
    public Date finalDate;

    @JacksonXmlProperty(localName = "IS_LONG_TIME_STORE", isAttribute = true)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    public Boolean isLongTimeStore;
}
