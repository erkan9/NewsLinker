package erkamber.validations;

import erkamber.configurations.CharAndNumberPatternConfiguration;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class NewsValidation {

    private final CharAndNumberPatternConfiguration charAndNumberPatternConfiguration;

    public NewsValidation(CharAndNumberPatternConfiguration charAndNumberPatternConfiguration) {
        this.charAndNumberPatternConfiguration = charAndNumberPatternConfiguration;
    }

    public boolean isNewsTitleValid(String newsTitle) {

        String charAndNumberPattern = charAndNumberPatternConfiguration.getCharAndNumberPattern();

        return Pattern.compile(charAndNumberPattern).matcher(newsTitle).matches();
    }
}
