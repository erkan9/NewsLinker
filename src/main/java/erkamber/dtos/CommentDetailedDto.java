package erkamber.dtos;

import erkamber.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDetailedDto {

    @Positive
    private int commendID;

    @Positive
    private UserDto author;

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
}
