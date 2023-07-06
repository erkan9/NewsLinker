package erkamber.mappers;

import erkamber.dtos.CommentDetailedDto;
import erkamber.dtos.CommentDto;
import erkamber.dtos.UserDto;
import erkamber.entities.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDto mapCommentToCommentDto(Comment comment) {

        return new CommentDto(comment.getCommendID(), comment.getCommentNewsID(), comment.getCommentAuthorID(),
                comment.getCommentContent(), comment.getCommentUpVotes(), comment.getCommentDownVotes(),
                comment.getCreationDate());
    }

    public Comment mapCommentToCommentDto(CommentDto commentDto) {

        return new Comment(commentDto.getCommendID(), commentDto.getCommentNewsID(), commentDto.getCommentAuthorID(),
                commentDto.getCommentContent(), commentDto.getCommentUpVotes(), commentDto.getCommentDownVotes(),
                commentDto.getCreationDate());
    }

    public CommentDetailedDto mapToCommentDetailedDto(CommentDto commentDto, UserDto userDto) {

        return new CommentDetailedDto(commentDto.getCommendID(), userDto, commentDto.getCommentNewsID(),
                commentDto.getCommentContent(), commentDto.getCommentUpVotes(), commentDto.getCommentDownVotes(),
                commentDto.getCreationDate());
    }
}
