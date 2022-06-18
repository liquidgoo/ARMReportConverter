import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Template {

    @JacksonXmlProperty(localName = "DESCRIPTION")
    public TemplateDescription description;

    @JacksonXmlProperty(localName = "PARTS")
    public ListWrapper<TemplatePart> parts = new ListWrapper<>();

    private ListWrapper<TemplatePart> getParts() {
        return parts;
    }

    public void setParts(ListWrapper<TemplatePart> parts) {
        this.parts = parts;
    }

    @JacksonXmlProperty(localName = "REFERENCES")
    private ListWrapper<ReferenceList> referenceLists = new ListWrapper<>();

    public List<ReferenceList> getReferenceLists() {
        return referenceLists.list;
    }

    public void setReferenceLists(List<ReferenceList> referenceLists) {
        this.referenceLists.list = referenceLists;
    }

    @JacksonXmlProperty(localName = "UNIT_OF_MEASURE")
    private ListWrapper<UnitOfMeasure> unitsOfMeasure = new ListWrapper<>();

    public List<UnitOfMeasure> getUnitsOfMeasure() {
        return unitsOfMeasure.list;
    }

    public void setUnitsOfMeasure(List<UnitOfMeasure> unitsOfMeasure) {
        this.unitsOfMeasure.list = unitsOfMeasure;
    }

    @JacksonXmlProperty(localName = "PERIOD_TYPE")
    public PeriodType periodType;
}
