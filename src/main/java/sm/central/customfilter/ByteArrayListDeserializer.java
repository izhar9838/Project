package sm.central.customfilter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ByteArrayListDeserializer extends JsonDeserializer<List<byte[]>> {
    @Override
    public List<byte[]> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<String> base64Strings = p.readValueAs(new com.fasterxml.jackson.core.type.TypeReference<List<String>>(){});
        List<byte[]> result = new ArrayList<>();
        if (base64Strings != null) {
            for (String base64 : base64Strings) {
                if (base64 != null) {
                    String cleanBase64 = base64.replaceFirst("^data:image/[^;]+;base64,", "");
                    result.add(Base64.getDecoder().decode(cleanBase64));
                }
            }
        }
        return result;
    }
}