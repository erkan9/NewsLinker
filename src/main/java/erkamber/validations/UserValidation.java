package erkamber.validations;


import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserValidation {

    public boolean isUserNameValid(String usernName) {

        String usernamePattern = "([A-Za-z_]{5,15})\\w+";

        return Pattern.compile(usernamePattern).matcher(usernName).matches();
    }

    public boolean isUserPasswordValid(String password) {

        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@-_~])[A-Za-z@-_~|]{6,20}$";

        return Pattern.compile(passwordPattern).matcher(password).matches();
    }

    public boolean isUserFirstOrLastNameValid(String name) {

        String passwordPattern = "^[A-Za-z]+$";

        return Pattern.compile(passwordPattern).matcher(name).matches();

    }
}
