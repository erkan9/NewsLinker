package erkamber.validations;

import erkamber.configurations.InjectionPatternConfiguration;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class InjectionValidation {

    private final InjectionPatternConfiguration injectionPatternConfiguration;

    public InjectionValidation(InjectionPatternConfiguration injectionPatternConfiguration) {
        this.injectionPatternConfiguration = injectionPatternConfiguration;
    }

    public boolean isTextContainingInjection(String text) {

        String injectionPattern = injectionPatternConfiguration.getInjectionPattern();

        return Pattern.compile(injectionPattern).matcher(text).matches();
    }

    public boolean isTextContainingSqlInjection(String text) {

        String sqlInjectionPattern = injectionPatternConfiguration.getSqlInjectionPattern();

        return Pattern.compile(sqlInjectionPattern).matcher(text).matches();
    }
}
