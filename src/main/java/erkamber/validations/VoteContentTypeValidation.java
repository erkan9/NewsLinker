package erkamber.validations;


import erkamber.enums.VoteTypeComment;
import erkamber.enums.VoteTypeNews;
import org.springframework.stereotype.Component;

@Component
public class VoteContentTypeValidation {

    public boolean isContentTypeValid(String votedContentType) {

        String contentTypeComment = VoteTypeComment.COMMENT.getType();

        String contentTypeNews = VoteTypeNews.NEWS.getType();

        return votedContentType.equalsIgnoreCase(contentTypeComment) || votedContentType.equalsIgnoreCase(contentTypeNews);
    }
}
