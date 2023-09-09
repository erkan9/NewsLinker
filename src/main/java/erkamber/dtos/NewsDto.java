package erkamber.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class NewsDto {

    @PositiveOrZero(message = "News ID must be Positive number ")
    private int newsID;

    @Positive(message = "User ID must be Positive number ")
    private int userID;

    @NotBlank(message = "News Title cannot be Blank")
    @NotEmpty(message = "News Title cannot be Empty")
    @NotNull(message = "News Title cannot be Null")
    @Size(min = 5, max = 30, message = "News Title must be between 5 and 30 characters long")
    private String newsTitle;

    @NotBlank(message = "News Content cannot be Blank")
    @NotEmpty(message = "News Content cannot be Empty")
    @NotNull(message = "News Content cannot be Null")
    @Size(min = 5, max = 255, message = "News Content must be between 5 and 255 characters long")
    private String newsContent;

    @PositiveOrZero(message = "Up votes must be Positive number or Zero")
    private int newsUpVotes;

    @PositiveOrZero(message = "Down votes must be Positive number or Zero")
    private int newsDownVotes;

    private LocalDateTime newsCreationDate;

    public NewsDto() {
    }

    public NewsDto(
            int userID, String newsTitle, String newsContent, int newsUpVotes, int newsDownVotes,
            LocalDateTime newsCreationDate) {
        this.userID = userID;
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
        this.newsUpVotes = newsUpVotes;
        this.newsDownVotes = newsDownVotes;
        this.newsCreationDate = newsCreationDate;
    }

    public NewsDto(int userID, String newsTitle, String newsContent, int newsUpVotes, int newsDownVotes) {
        this.userID = userID;
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
        this.newsUpVotes = newsUpVotes;
        this.newsDownVotes = newsDownVotes;
    }

    public NewsDto(int userID, String newsTitle, String newsContent) {
        this.userID = userID;
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
    }

    public NewsDto(
            int newsID, int userID, String newsTitle, String newsContent, int newsUpVotes, int newsDownVotes,
            LocalDateTime newsCreationDate) {
        this.newsID = newsID;
        this.userID = userID;
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
        this.newsUpVotes = newsUpVotes;
        this.newsDownVotes = newsDownVotes;
        this.newsCreationDate = newsCreationDate;
    }
}
