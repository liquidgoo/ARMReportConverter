import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] argv) throws FileNotFoundException {
        ReportParser p = new ReportParser();
        FileInputStream reportStream = new FileInputStream("src\\main\\resources\\rep.xml");
        XmlReportData r = (XmlReportData) p.parseXmlReport(reportStream);


        FileInputStream templateStream = new FileInputStream("src\\main\\resources\\templ.xml");
        TemplateParser tp = new TemplateParser();
        XmlTemplateData t = (XmlTemplateData) tp.parseXmlReport(templateStream, r);
    }
}
