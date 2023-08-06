package erkamber.validations;

import erkamber.configurations.NewsTitlePatternConfiguration;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class NewsValidation {

    private final NewsTitlePatternConfiguration newsTitlePatternConfiguration;

    public NewsValidation(NewsTitlePatternConfiguration newsTitlePatternConfiguration) {
        this.newsTitlePatternConfiguration = newsTitlePatternConfiguration;
    }

    public boolean isNewsTitleValid(String newsTitle) {

        String charAndNumberPattern = newsTitlePatternConfiguration.getNewsTitlePattern();

        return Pattern.compile(charAndNumberPattern).matcher(newsTitle).matches();
    }

    public boolean isUserTheAuthorOfNews(int authorID, int userID) {

        return authorID == userID;
    }
}
