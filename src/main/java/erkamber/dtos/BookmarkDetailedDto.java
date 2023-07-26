package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDetailedDto {

    @Positive
    private int bookmarkID;

    @NotNull(message = "User cannot be Null")
    private UserDto user;

    @NotNull(message = "News cannot be Null")
    private NewsDetailedDto news;

}
