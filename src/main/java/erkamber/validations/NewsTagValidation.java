package erkamber.validations;

import erkamber.entities.NewsTag;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsTagValidation {

    public boolean isListEmpty(List<NewsTag> listOfNews) {

        return listOfNews == null || listOfNews.isEmpty();
    }
}
