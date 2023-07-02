package erkamber.validations;


import erkamber.configurations.OnlyCharPatternConfiguration;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class TagValidation {

    private final OnlyCharPatternConfiguration onlyCharPatternConfiguration;

    public TagValidation(OnlyCharPatternConfiguration onlyCharPatternConfiguration) {
        this.onlyCharPatternConfiguration = onlyCharPatternConfiguration;
    }

    public boolean isTagNameValid(String tagName) {

        String nameTagPattern = onlyCharPatternConfiguration.getOnlyCharPattern();

        return Pattern.compile(nameTagPattern).matcher(tagName).matches();

    }
}
