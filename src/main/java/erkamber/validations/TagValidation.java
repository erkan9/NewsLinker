package erkamber.validations;


import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class TagValidation {

    public boolean isTagNameValid(String tagName) {

        String passwordPattern = "^[A-Za-z]+$";

        return Pattern.compile(passwordPattern).matcher(tagName).matches();

    }
}
