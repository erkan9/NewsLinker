package erkamber.configurations;

import org.springframework.context.annotation.Configuration;

@Configuration
public class NewsTitlePatternConfiguration {

    String newsTitlePattern = "^[a-zA-Z0-9?!,.\\s]+$";

    public String getNewsTitlePattern() {

        return newsTitlePattern;
    }
}
