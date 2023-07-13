package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsTagDetailedDto {

    @Positive
    private int newsTagID;

    @Positive
    private NewsDetailedDto newsDetailedDto;

    @Positive
    private TagDto tagDto;


}
