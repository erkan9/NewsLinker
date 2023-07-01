package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDetailedDto {

    @Positive(message = "News ID must be Positive number")
    private int newsID;

    @NotNull
    private UserDto userDto;

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

    @NotNull(message = "News Creation Date cannot be Null")
    private LocalDate newsCreationDate;

    private List<TagDto> listOfNewsTags;

    private List<MediaDto> listOfNewsMedia;
}
