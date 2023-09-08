package erkamber.mappers;

import erkamber.dtos.CommentDetailedDto;
import erkamber.dtos.CommentDto;
import erkamber.dtos.UserDto;
import erkamber.entities.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDto mapCommentToCommentDto(Comment comment) {

        return new CommentDto(comment.getCommentID(), comment.getCommentAuthorID(), comment.getCommentNewsID(),
                comment.getCommentContent(), comment.getCommentUpVotes(), comment.getCommentDownVotes(),
                comment.getCreationDate());
    }

    public Comment mapCommentDtoToComment(CommentDto commentDto) {

        return new Comment(commentDto.getCommentID(), commentDto.getCommentAuthorID(), commentDto.getCommentNewsID(),
                commentDto.getCommentContent(), commentDto.getCommentUpVotes(), commentDto.getCommentDownVotes(),
                commentDto.getCreationDate());
    }

    public CommentDetailedDto mapToCommentDetailedDto(Comment comment, UserDto userDto) {

        return new CommentDetailedDto(comment.getCommentID(), userDto, comment.getCommentNewsID(),
                comment.getCommentContent(), comment.getCommentUpVotes(), comment.getCommentDownVotes(),
                comment.getCreationDate());
    }
}
