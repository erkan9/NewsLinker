package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {

    @Positive
    private int tagID;

    @NotBlank(message = "Tag Name cannot be Blank")
    @NotEmpty(message = "Tag Name cannot be Empty")
    @NotNull(message = "Tag Name cannot be Null")
    @Size(max = 40, message = "Tag Name must be up to 40 characters long")
    private String tagName;

    public TagDto(String tagName) {
        this.tagName = tagName;
    }
}
