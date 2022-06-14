import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.Map;

public class TemplateParser extends XmlParser {

    //TODO: ref list
    //TODO: unit of measure
    //TODO: replace bools with method calls?
    public XmlParserResult parseXmlReport(InputStream stream, XmlReportData reportData) {
        boolean extAttr = false;
        boolean parts = false;
        boolean tables = false;
        boolean graphs = false;
        boolean rows = false;
        boolean deathCells = false;

        String part_id = null;
        String table_id = null;

        String period_id = reportData.description.get("ID_P");

        XmlTemplateData template = new XmlTemplateData();

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(stream); //TODO: encoding?

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT: {
                        StartElement startElement = event.asStartElement();
                        String elementName = startElement.getName().getLocalPart();

                        Map<String, String> elementData = getElementAttributes(startElement);

                        //TODO: const strings
                        switch (elementName) {
                            case "DESCRIPTION":
                                template.description = elementData;
                                break;
                            case "FORM_EXTATTR":
                                extAttr = true;
                                break;
                            case "PARTS":
                                parts = true;
                                break;
                            case "TABLES":
                                tables = true;
                                parts = false;
                                break;
                            case "GRAPH_PROGRAPH":
                                graphs = true;
                                tables = false;
                                break;
                            case "ROW_IN_TABLE":
                                rows = true;
                                tables = false;
                                break;
                            case "DEATH_GRAPH_CELL":
                                deathCells = true;
                                tables = false;
                                break;
                            case "row":
                                if (extAttr) template.extAttr.add(elementData);
                                else if (parts) {
                                    template.parts.add(elementData);
                                    part_id = elementData.get("ID_DPART");
                                }
                                else if (tables) {
                                    elementData.put("ID_DPART", part_id);
                                    template.tables.add(elementData);
                                    table_id = elementData.get("ID_DTABLE");
                                }
                                else if (graphs || rows || deathCells) {
                                    elementData.put("ID_DTABLE", table_id);
                                    if (graphs) template.graphs.add(elementData);
                                    else if (rows) template.rows.add(elementData);
                                    else template.deathCells.add(elementData);
                                }
                                break;
                            case "PERIOD":
                                if (elementData.get("ID_P").equals(period_id)) template.period = elementData;
                                break;
                        }
                        break;
                    }
                    case XMLStreamConstants.END_ELEMENT:
                        EndElement endElement = event.asEndElement();
                        String elementName = endElement.getName().getLocalPart();

                        //TODO: const strings
                        switch (elementName) {
                            case "FORM_EXTATTR":
                                extAttr = false;
                                break;
                            case "PARTS":
                                parts = false;
                                break;
                            case "TABLES":
                                tables = false;
                                parts = true;
                                break;
                            case "GRAPH_PROGRAPH":
                                graphs = false;
                                tables = true;
                                break;
                            case "ROW_IN_TABLE":
                                rows = false;
                                tables = true;
                                break;
                            case "DEATH_GRAPH_CELL":
                                deathCells = false;
                                tables = true;
                                break;
                        }
                        break;
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        return template;
    }

    @Override
    public XmlParserResult parseXmlReport(InputStream stream) {
        return parseXmlReport(stream, null);
    }
}
