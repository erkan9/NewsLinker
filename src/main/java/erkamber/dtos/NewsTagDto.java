package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsTagDto {

    @PositiveOrZero
    private int newsTagID;

    @Positive
    private int newsID;

    @Positive
    private int tagID;

    public NewsTagDto(int newsID, int tagID) {
        this.newsID = newsID;
        this.tagID = tagID;
    }
}
