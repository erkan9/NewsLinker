package erkamber.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserLoginDto {

    @NotBlank(message = "Email cannot be Blank!")
    @Size(min = 1, message = "Email cannot be Empty!")
    private String userEmail;

    @NotBlank(message = "User Password cannot be Blank!")
    @Size(min = 1, message = "User Password cannot be Empty!")
    private String userPassword;

    public UserLoginDto() {
    }

    public UserLoginDto(String userEmail, String userPassword) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }
}
