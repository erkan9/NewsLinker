package erkamber.configurations;

import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordPatternConfiguration {

    String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@-_~])[A-Za-z@-_~|]{6,20}$";

    public String getPasswordPattern() {

        return passwordPattern;
    }
}
