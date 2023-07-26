package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDto {

    @Positive
    private int bookmarkID;

    @Positive
    private int userID;

    @Positive
    private int newsID;

}
