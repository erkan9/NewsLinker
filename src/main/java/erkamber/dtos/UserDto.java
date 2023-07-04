package erkamber.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Positive
    private int userID;

    @NotBlank(message = "First Name cannot be Blank")
    @NotEmpty(message = "First Name cannot be Empty")
    @NotNull(message = "First Name cannot be Null")
    @Size(max = 20, message = "First Name must be up to 20 characters long")
    private String userFirstName;

    @NotBlank(message = "Last Name cannot be Blank")
    @NotEmpty(message = "Last Name cannot be Empty")
    @NotNull(message = "Last Name cannot be Null")
    @Size(max = 20, message = "Last Name must be up to 20 characters long")
    private String userLastName;

    @NotBlank(message = "Username cannot be Blank")
    @NotEmpty(message = "Username cannot be Empty")
    @NotNull(message = "Username cannot be Null")
    @Size(max = 20, message = "Username must be up to 20 characters long")
    private String userName;

    @NotBlank(message = "Email cannot be Blank")
    @NotEmpty(message = "Email cannot be Empty")
    @NotNull(message = "Email cannot be Null")
    @Size(max = 30, message = "Email must be up to 30 characters long")
    @Email(message = "Email is not Valid")
    private String userEmail;

    @NotBlank(message = "Password cannot be Blank")
    @NotEmpty(message = "Password cannot be Empty")
    @NotNull(message = "Password cannot be Null")
    @Size(min = 5, max = 35, message = "Password must be 5 and 35 characters long")
    private String userPassword;

    private String userPhoto;

    private boolean isUserReporter;
}
