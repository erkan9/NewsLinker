package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {

    @Positive(message = "Up Vote ID must be positive")
    private int voteID;

    @Positive(message = "Voted content ID must be positive")
    private int votedContentID;

    @Positive(message = "User ID must be positive")
    private int userID;

    @NotBlank(message = "Up Voted content Type cannot be Blank")
    @NotEmpty(message = "Up Voted content Type cannot be Empty")
    @NotNull(message = "Up Voted content Type cannot be Null")
    @Size(min = 4, max = 7, message = "Up Voted content Type must be between 4 to 7 characters long")
    private String votedContentType;

    private boolean isUpVote;
}
