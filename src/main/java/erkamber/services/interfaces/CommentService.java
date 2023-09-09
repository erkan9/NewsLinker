package erkamber.services.interfaces;

import erkamber.dtos.CommentDetailedDto;
import erkamber.dtos.CommentDto;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;

public interface CommentService {

    int addNewComment(CommentDto commentDto) throws MessagingException;

    void updateCommentContent(int commentID, int userID, String newContent);

    void deleteCommentByCommentID(int commentID);

    void deleteAllCommentsOfUser(int userID);

    void deleteAllCommentsOfNewsID(int newsID);

    CommentDetailedDto getCommentByID(int commentID);

    List<CommentDetailedDto> getCommentsByUserID(int userID);

    List<CommentDetailedDto> getCommentsByNewsID(int newsID);

    List<CommentDetailedDto> getCommentsByNewsIDAndIDCreationDate(int newsID, LocalDate creationDate);

    List<CommentDetailedDto> getCommentsByNewsIDAndCreationDateBefore(int newsID, LocalDate creationDate);

    List<CommentDetailedDto> getCommentsByNewsIDAndCreationDateAfter(int newsID, LocalDate creationDate);

    List<CommentDetailedDto> getAllComments();
}
