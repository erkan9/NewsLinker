package erkamber.validations;


import erkamber.configurations.CharAndNumberPatternConfiguration;
import erkamber.configurations.OnlyCharPatternConfiguration;
import erkamber.configurations.PasswordEncoderConfiguration;
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

    private final PasswordEncoderConfiguration passwordEncoderConfiguration;

    public UserValidation(OnlyCharPatternConfiguration onlyCharPatternConfiguration,
                          CharAndNumberPatternConfiguration charAndNumberPatternConfiguration,
                          PasswordPatternConfiguration passwordPatternConfiguration,
                          PasswordEncoderConfiguration passwordEncoderConfiguration) {

        this.onlyCharPatternConfiguration = onlyCharPatternConfiguration;
        this.charAndNumberPatternConfiguration = charAndNumberPatternConfiguration;
        this.passwordPatternConfiguration = passwordPatternConfiguration;
        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
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

    public boolean isListEmpty(List<User> listOfNews) {

        return listOfNews == null || listOfNews.isEmpty();
    }

    public boolean areOldPasswordMatching(String oldPassword, String oldPasswordEncoded) {

        return  passwordEncoderConfiguration.passwordEncoder().matches(oldPassword, oldPasswordEncoded);
    }

    public boolean areNewPasswordsMatching(String newPassword, String newPasswordRepeat) {

        return newPassword.equals(newPasswordRepeat);
    }
}
