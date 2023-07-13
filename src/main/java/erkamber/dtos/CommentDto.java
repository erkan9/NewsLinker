package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @Positive
    private int commentID;

    @Positive
    private int commentAuthorID;

    @Positive
    private int commentNewsID;

    @NotBlank(message = "Comment cannot be Blank")
    @NotEmpty(message = "Comment cannot be Empty")
    @NotNull(message = "Comment cannot be Null")
    @Size(min = 2, max = 300, message = "Comment must be between 2 and 300 characters long")
    private String commentContent;

    @PositiveOrZero(message = "Up votes must be Positive number or Zero")
    private int commentUpVotes;

    @PositiveOrZero(message = "Down votes must be Positive number or Zero")
    private int commentDownVotes;

    private LocalDate creationDate;

    public CommentDto(int commentAuthorID, int commentNewsID, String commentContent, int commentUpVotes, int commentDownVotes, LocalDate creationDate) {
        this.commentAuthorID = commentAuthorID;
        this.commentNewsID = commentNewsID;
        this.commentContent = commentContent;
        this.commentUpVotes = commentUpVotes;
        this.commentDownVotes = commentDownVotes;
        this.creationDate = creationDate;
    }
}
