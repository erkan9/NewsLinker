package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDto {

    private int bookmarkID;

    @Positive(message = "UserID must be a Positive number!")
    private int userID;

    @Positive(message = "NewsID must be a Positive number!")
    private int newsID;

}
