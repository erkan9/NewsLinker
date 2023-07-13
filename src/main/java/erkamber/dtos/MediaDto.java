package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDto {

    @Positive
    private int mediaID;

    @Positive
    private int newsID;

    @NotBlank(message = "Media String cannot be Blank")
    @NotEmpty(message = "Media String cannot be Empty")
    @NotNull(message = "Media String cannot be Null")
    @Size(max = 255, message = "Media String must be up to 255 characters long")
    private String mediaString;

    public MediaDto(int newsID, String mediaString) {
        this.newsID = newsID;
        this.mediaString = mediaString;
    }
}
