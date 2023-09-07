package erkamber.configurations;

import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordPatternConfiguration {

    String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@-_~])(?=.*[0-9])[A-Za-z@-_~0-9|]{6,20}$";

    public String getPasswordPattern() {

        return passwordPattern;
    }
}
