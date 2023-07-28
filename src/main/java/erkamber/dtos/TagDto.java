package erkamber.dtos;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
public class TagDto {

    @PositiveOrZero(message = "Tag ID must be a Positive Number")
    private int tagID;

    @NotBlank(message = "Tag Name cannot be Blank")
    @NotEmpty(message = "Tag Name cannot be Empty")
    @NotNull(message = "Tag Name cannot be Null")
    @Size(max = 40, message = "Tag Name must be up to 40 characters long")
    private String tagName;

    public TagDto() {
    }

    public TagDto(String tagName) {
        this.tagName = tagName;
    }

    public TagDto(int tagID, String tagName) {
        this.tagID = tagID;
        this.tagName = tagName;
    }
}
