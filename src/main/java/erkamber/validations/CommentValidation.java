package erkamber.validations;

import org.springframework.stereotype.Component;

@Component
public class CommentValidation {

    public boolean isUserIDMatchingCommentAuthorID(int authorID, int userID) {

        return authorID == userID;
    }
}
