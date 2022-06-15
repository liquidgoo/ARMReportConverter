package marshallingTypes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ReportCell {

    @JacksonXmlProperty(localName = "ID_ROWRPT", isAttribute = true)
    public int rowId;

    @JacksonXmlProperty(localName = "ID_DGP", isAttribute = true)
    public int graphId;

    @JacksonXmlProperty(localName = "VALUE_GCELL", isAttribute = true)
    public String value;

    @JacksonXmlProperty(localName = "ID_UOM", isAttribute = true)
    public int unitOfMeasure;

}
