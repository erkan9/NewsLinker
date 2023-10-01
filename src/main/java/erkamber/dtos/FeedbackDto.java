package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto {

    @PositiveOrZero
    int feedbackID;

    UserDto authorDto;

    @NotBlank(message = "Feedback Content cannot be Blank")
    @NotEmpty(message = "Feedback Content cannot be Empty")
    @NotNull(message = "Feedback Content cannot be Null")
    @Size(min = 2, max = 300, message = "Feedback Content length could be max 500 characters")
    private String feedbackContent;

    private LocalDateTime feedbackCreationDate;
}