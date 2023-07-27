package erkamber.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class HttpHeadersConfiguration {

    HttpHeaders headers = new HttpHeaders();

    public HttpHeaders getHeaders() {

        return headers;
    }
}
