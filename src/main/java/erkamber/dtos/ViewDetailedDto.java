package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewDetailedDto {

    @Positive
    private int viewID;

    @NotNull(message = "View Creation Date cannot be Null")
    private LocalDate viewCreationDate;

    private NewsDto newsDto;

    private UserDto userDto;
}
