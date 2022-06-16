package marshallingTypes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class Template {

    @JacksonXmlProperty(localName = "DESCRIPTION")
    public TemplateDescription description;

    @JacksonXmlProperty(localName = "PARTS")
    public ListWrapper<TemplatePart> parts;

    @JacksonXmlProperty(localName = "REFERENCES")
    public ListWrapper<ReferenceList> referenceLists;

    @JacksonXmlProperty(localName = "UNIT_OF_MEASURE")
    public ListWrapper<UnitOfMeasure> unitsOfMeasure;

    @JacksonXmlProperty(localName = "PERIOD_TYPE")
    public PeriodType periodType;
}
