package erkamber.validations;


import erkamber.configurations.CharAndNumberPatternConfiguration;
import erkamber.configurations.OnlyCharPatternConfiguration;
import erkamber.configurations.PasswordPatternConfiguration;
import erkamber.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserValidation {

    private final OnlyCharPatternConfiguration onlyCharPatternConfiguration;

    private final CharAndNumberPatternConfiguration charAndNumberPatternConfiguration;

    private final PasswordPatternConfiguration passwordPatternConfiguration;

    public UserValidation(OnlyCharPatternConfiguration onlyCharPatternConfiguration,
                          CharAndNumberPatternConfiguration charAndNumberPatternConfiguration,
                          PasswordPatternConfiguration passwordPatternConfiguration) {

        this.onlyCharPatternConfiguration = onlyCharPatternConfiguration;
        this.charAndNumberPatternConfiguration = charAndNumberPatternConfiguration;
        this.passwordPatternConfiguration = passwordPatternConfiguration;
    }

    public boolean isUserNameValid(String userName) {

        String userNamePattern = charAndNumberPatternConfiguration.getCharAndNumberPattern();

        return Pattern.compile(userNamePattern).matcher(userName).matches();
    }

    public boolean isUserPasswordValid(String password) {

        String passwordPattern = passwordPatternConfiguration.getPasswordPattern();

        return Pattern.compile(passwordPattern).matcher(password).matches();
    }

    public boolean isUserFirstOrLastNameValid(String name) {

        String userFirstLastNamePatterns = onlyCharPatternConfiguration.getOnlyCharPattern();

        return Pattern.compile(userFirstLastNamePatterns).matcher(name).matches();

    }

    public boolean isUserExistsByUserID(List<User> allUsers, int searchedUserID) {

        return allUsers.stream().anyMatch(user -> user.getUserID() == searchedUserID);
    }
}
