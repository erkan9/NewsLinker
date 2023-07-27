package erkamber.dtos;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
public class MediaDto {

    @PositiveOrZero
    private int mediaID;

    @Positive(message = "NewsID must be a Positive number!")
    private int newsID;

    @NotBlank(message = "Media String cannot be Blank")
    @NotEmpty(message = "Media String cannot be Empty")
    @NotNull(message = "Media String cannot be Null")
    @Size(max = 255, message = "Media String must be up to 255 characters long")
    private String mediaString;

    public MediaDto() {
    }

    public MediaDto(int newsID, String mediaString) {
        this.newsID = newsID;
        this.mediaString = mediaString;
    }

    public MediaDto(int mediaID, int newsID, String mediaString) {
        this.mediaID = mediaID;
        this.newsID = newsID;
        this.mediaString = mediaString;
    }
}
