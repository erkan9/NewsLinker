package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsTagDto {

    @Positive
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
