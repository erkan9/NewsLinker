package erkamber.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

public class ViewDetailedDto {

    @Positive
    private int viewID;

    @NotNull(message = "View Creation Date cannot be Null")
    private LocalDate viewCreationDate;

    private NewsDto newsDto;

    private UserDto userDto;
}
