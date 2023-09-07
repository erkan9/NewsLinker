package erkamber.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserPasswordUpdateRequestDto {

    @NotBlank(message = "Old Password cannot be Blank")
    @NotNull(message = "Old Password cannot be Null")
    @NotEmpty(message = "Old Password cannot be Empty")
    @Size(min = 6, max = 20, message = "Old Password must be between 6 and 20 characters long")
    private String oldPassword;

    @NotBlank(message = "New Password cannot be blank")
    @NotNull(message = "New Password cannot be Null")
    @NotEmpty(message = "New Password cannot be Empty")
    @Size(min = 6, max = 20, message = "New Password must be between 6 and 20 characters long")
    private String newPassword;

    @NotBlank(message = "Repeat New Password Repeat cannot be blank")
    @NotNull(message = "Repeat New Password cannot be Null")
    @NotEmpty(message = "Repeat New Password cannot be Empty")
    @Size(min = 6, max = 20, message = "New Password Repeat must be between 6 and 20 characters long")
    private String newPasswordRepeat;
}
