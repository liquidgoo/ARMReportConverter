import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XmlReportData extends XmlParserResult {
    public Map<String, String> description;
    public Map<String, String> userInfo;
    public List<Map<String, String>> rows = new ArrayList<>(); //TODO: classes?
    public List<Map<String, String>> cells = new ArrayList<>();
}
