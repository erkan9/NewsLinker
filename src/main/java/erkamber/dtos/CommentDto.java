package erkamber.dtos;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
public class CommentDto {

    @PositiveOrZero
    private int commentID;

    @Positive(message = "Comment Author ID must be a positive number!")
    private int commentAuthorID;

    @Positive(message = "Comment News ID must be a positive number!")
    private int commentNewsID;

    @NotBlank(message = "Comment cannot be Blank")
    @NotEmpty(message = "Comment cannot be Empty")
    @NotNull(message = "Comment cannot be Null")
    @Size(min = 2, max = 300, message = "Comment must be between 2 and 300 characters long")
    private String commentContent;

    @PositiveOrZero(message = "Up votes must be Positive number or Zero")
    private int commentUpVotes = 0;

    @PositiveOrZero(message = "Down votes must be Positive number or Zero")
    private int commentDownVotes = 0;

    private LocalDate creationDate;

    public CommentDto() {
    }

    public CommentDto(int commentAuthorID, int commentNewsID, String commentContent) {
        this.commentAuthorID = commentAuthorID;
        this.commentNewsID = commentNewsID;
        this.commentContent = commentContent;
    }

    public CommentDto(int commentAuthorID, int commentNewsID, String commentContent, int commentUpVotes, int commentDownVotes, LocalDate creationDate) {
        this.commentAuthorID = commentAuthorID;
        this.commentNewsID = commentNewsID;
        this.commentContent = commentContent;
        this.commentUpVotes = commentUpVotes;
        this.commentDownVotes = commentDownVotes;
        this.creationDate = creationDate;
    }

    public CommentDto(int commentID, int commentAuthorID, int commentNewsID, String commentContent, int commentUpVotes, int commentDownVotes, LocalDate creationDate) {
        this.commentID = commentID;
        this.commentAuthorID = commentAuthorID;
        this.commentNewsID = commentNewsID;
        this.commentContent = commentContent;
        this.commentUpVotes = commentUpVotes;
        this.commentDownVotes = commentDownVotes;
        this.creationDate = creationDate;
    }
}
