package erkamber.requests;

import erkamber.dtos.TagDto;
import lombok.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsTagDtoResponse {

    @PositiveOrZero
    private int newsTagID;

    @Positive
    private int newsID;

    @Positive
    private TagDto tagDto;
}
