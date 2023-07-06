package erkamber.services.interfaces;

import erkamber.dtos.CommentDetailedDto;
import erkamber.dtos.CommentDto;

import java.time.LocalDate;
import java.util.List;

public interface CommentService {

    int addNewComment(CommentDto commentDto);

    int addCommentUpVote(int commentID, int userID);

    int addCommentDownVote(int commentID, int userID);

    void updateCommentContent(int commentID, int userID, String newContent);

    void deleteCommentByCommentID(int commentID);

    void deleteCommentByUserID(int userID);

    CommentDetailedDto getCommentByID(int commentID);

    List<CommentDetailedDto> getCommentsByUserID(int userID);

    List<CommentDetailedDto> getCommentsByCreationDate(LocalDate creationDate);

    List<CommentDetailedDto> getCommentsByCreationDateBefore(LocalDate creationDate);

    List<CommentDetailedDto> getCommentsByCreationDateAfter(LocalDate creationDate);

    List<CommentDetailedDto> getAllComments();
}
