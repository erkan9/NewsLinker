package erkamber.validations;

import erkamber.configurations.NewsTitlePatternConfiguration;
import erkamber.entities.News;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public boolean isListEmpty(List<News> listOfNews) {

        return listOfNews == null || listOfNews.isEmpty();
    }
}
