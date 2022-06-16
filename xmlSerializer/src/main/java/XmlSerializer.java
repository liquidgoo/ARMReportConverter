import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;

public class XmlSerializer {

    class BooleanDeserializer extends StdDeserializer<Boolean> {

        protected BooleanDeserializer() {
            super(Boolean.class);
        }

        @Override
        public Boolean deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            return jsonParser.getValueAsInt() == 1;
        }
    }

    XmlMapper xmlMapper = new XmlMapper();

    private String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(is, "windows-1251"));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }


    private void registerCustomBooleanDeserializer() {
        SimpleModule module = new SimpleModule("configModule", com.fasterxml.jackson.core.Version.unknownVersion());
        module.addDeserializer(Boolean.class, new BooleanDeserializer());
        xmlMapper.registerModule(module);
    }

    private void registerCustomBooleanSerializer() {
        //TODO
    }

    public <T> T deserialize(InputStream inputStream, Class<T> valueType) throws IOException {
        return deserialize(inputStreamToString(inputStream), valueType);
    }

    public <T> T deserialize(String xmlString, Class<T> valueType) throws JsonProcessingException {
        return xmlMapper.readValue(xmlString, valueType);
    }

    public <T> String serializeToString(T Value) {
        //TODO
        return "";
    }

    public <T> void serializeToStream(T Value, OutputStream outputStream) {
        //TODO
    }


    public XmlSerializer() {
        registerCustomBooleanSerializer();
        registerCustomBooleanDeserializer();
    }
}
