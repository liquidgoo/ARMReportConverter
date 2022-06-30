import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Template {

    @JacksonXmlProperty(localName = "DESCRIPTION")
    public TemplateDescription description;

    @JacksonXmlProperty(localName = "PARTS")
    private ListWrapper<TemplatePart> parts = new ListWrapper<>();

    public List<TemplatePart> getParts() {
        return parts.list;
    }

    public void setParts(List<TemplatePart> parts) {
        this.parts.list = parts;
    }

    @JacksonXmlProperty(localName = "REFERENCES")
    private ListWrapper<ReferenceList> referenceLists = new ListWrapper<>();

    public List<ReferenceList> getReferenceLists() {
        return referenceLists.list;
    }

    public Map<Integer, ReferenceList> getReferenceListsAsMap() {
        Map<Integer, ReferenceList> map = new HashMap<>();
        referenceLists.list.forEach(refList -> map.put(refList.id, refList));
        return map;
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

    public Map<Integer, UnitOfMeasure> getUnitsOfMeasureAsMap() {
        Map<Integer, UnitOfMeasure> map = new HashMap<>();
        unitsOfMeasure.list.forEach(uom -> map.put(uom.id, uom));
        return map;
    }

    public UnitOfMeasure getUnitOfMeasure(int id) {
        for (UnitOfMeasure uom : unitsOfMeasure.list) {
            if (uom.id == id) return uom;
        }
        return null;
    }

    @JacksonXmlProperty(localName = "PERIOD_TYPE")
    public PeriodType periodType;
}
