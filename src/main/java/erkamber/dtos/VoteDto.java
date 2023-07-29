package erkamber.dtos;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
public class VoteDto {

    @PositiveOrZero(message = "Up Vote ID must be positive")
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

    private boolean upVote;

    public VoteDto() {
    }

    public VoteDto(int votedContentID, int userID, String votedContentType, boolean upVote) {
        this.votedContentID = votedContentID;
        this.userID = userID;
        this.votedContentType = votedContentType;
        this.upVote = upVote;
    }

    public VoteDto(int voteID, int votedContentID, int userID, String votedContentType, boolean upVote) {
        this.voteID = voteID;
        this.votedContentID = votedContentID;
        this.userID = userID;
        this.votedContentType = votedContentType;
        this.upVote = upVote;
    }
}
