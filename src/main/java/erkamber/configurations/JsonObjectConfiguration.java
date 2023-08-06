package erkamber.configurations;

import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonObjectConfiguration {

    public JSONObject getJsonObjectConfiguration(String text) {

        return new JSONObject(text);
    }
}
