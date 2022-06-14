import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class XmlParser {
    public abstract XmlParserResult parseXmlReport(InputStream stream);

    protected Map<String, String> getElementAttributes(StartElement element) {
        Map<String, String> elementData = new HashMap<>();
        Iterator<Attribute> attributes = element.getAttributes();
        while (attributes.hasNext()) {
            Attribute currentAt = attributes.next();
            elementData.put(currentAt.getName().toString(), currentAt.getValue());
        }

        return elementData;
    }
}
