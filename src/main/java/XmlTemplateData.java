import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XmlTemplateData extends XmlParserResult  {

    public Map<String, String> description;
    public List<Map<String, String>> extAttr = new ArrayList<>();

    //TODO: classes?
    public List<Map<String, String>> parts = new ArrayList<>();
    public List<Map<String, String>> tables = new ArrayList<>();
    public List<Map<String, String>> graphs = new ArrayList<>();
    public List<Map<String, String>> rows = new ArrayList<>();
    public List<Map<String, String>> deathCells = new ArrayList<>();


    public Map<String, String> period;
}
