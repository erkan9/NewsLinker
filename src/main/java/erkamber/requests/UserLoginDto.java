package erkamber.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserLoginDto {

    @NotBlank(message = "UserName cannot be Blank!")
    @Size(min = 1, message = "UserName cannot be Empty!")
    private String userName;

    @NotBlank(message = "User Password cannot be Blank!")
    @Size(min = 1, message = "User Password cannot be Empty!")
    private String userPassword;

    public UserLoginDto() {
    }

    public UserLoginDto(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }
}
