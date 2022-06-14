import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.XMLStreamConstants;
import java.io.InputStream;
import java.util.Map;

public class ReportParser extends XmlParser{

    @Override
    public XmlParserResult parseXmlReport(InputStream stream) {
        boolean rows = false;

        XmlReportData reportData = new XmlReportData();

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(stream); //TODO: encoding?

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT: {
                        StartElement startElement = event.asStartElement();
                        String elementName = startElement.getName().getLocalPart();



                        //TODO: const strings
                        if (elementName.equals("ROW_REPORT")) {
                            rows = true;
                        } else {
                            Map<String, String> elementData = getElementAttributes(startElement);

                            switch (elementName) {
                                case "DESCRIPTION":
                                    reportData.description = elementData;
                                    break;
                                case "USERS":
                                    reportData.userInfo = elementData;
                                    break;
                                case "row":
                                    if (rows) {
                                        reportData.rows.add(elementData);
                                    } else {
                                        reportData.cells.add(elementData);
                                    }
                                    break;
                            }
                        }
                        break;
                    }
                    case XMLStreamConstants.END_ELEMENT:
                        EndElement endElement = event.asEndElement();
                        String elementName = endElement.getName().getLocalPart();

                        if (elementName.equals("ROW_REPORT")) {
                            rows = false;
                        }
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        return reportData;
    }
}
