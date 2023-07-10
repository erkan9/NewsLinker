package erkamber.mappers;

import erkamber.dtos.CommentDetailedDto;
import erkamber.dtos.CommentDto;
import erkamber.entities.Comment;
import erkamber.entities.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDto mapCommentToCommentDto(Comment comment) {

        return new CommentDto(comment.getCommentID(), comment.getCommentNewsID(), comment.getCommentAuthorID(),
                comment.getCommentContent(), comment.getCommentUpVotes(), comment.getCommentDownVotes(),
                comment.getCreationDate());
    }

    public Comment mapCommentDtoToComment(CommentDto commentDto) {

        return new Comment(commentDto.getCommentID(), commentDto.getCommentNewsID(), commentDto.getCommentAuthorID(),
                commentDto.getCommentContent(), commentDto.getCommentUpVotes(), commentDto.getCommentDownVotes(),
                commentDto.getCreationDate());
    }

    public CommentDetailedDto mapToCommentDetailedDto(Comment comment, User user) {

        return new CommentDetailedDto(comment.getCommentID(), user, comment.getCommentNewsID(),
                comment.getCommentContent(), comment.getCommentUpVotes(), comment.getCommentDownVotes(),
                comment.getCreationDate());
    }
}
