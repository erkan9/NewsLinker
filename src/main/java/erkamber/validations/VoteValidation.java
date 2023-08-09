package erkamber.validations;


import erkamber.entities.Vote;
import erkamber.enums.VoteTypeComment;
import erkamber.enums.VoteTypeNews;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VoteValidation {

    public boolean isContentTypeValid(String votedContentType) {

        String contentTypeComment = VoteTypeComment.COMMENT.getType();

        String contentTypeNews = VoteTypeNews.NEWS.getType();

        return votedContentType.equalsIgnoreCase(contentTypeComment) || votedContentType.equalsIgnoreCase(contentTypeNews);
    }

    public boolean isVoteListEmpty(List<Vote> listOfVotes) {

        return listOfVotes == null || listOfVotes.isEmpty();
    }
}
