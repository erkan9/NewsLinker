package erkamber.controllers;

import erkamber.dtos.UserDto;
import erkamber.requests.UserLoginDto;
import erkamber.requests.UserPasswordUpdateRequestDto;
import erkamber.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://news-linker-fe.vercel.app"})
@RestController
@RequestMapping("/api/v1")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/register")
    public ResponseEntity<Void> addNewUser(@Valid @RequestBody UserDto userDto) {

        int newUserID = userService.registerUser(userDto);

        return ResponseEntity.created(URI.create("api/v1/users/register" + newUserID)).build();
    }

    @PatchMapping("/users/update/")
    public ResponseEntity<String> updateUser(@RequestParam @Positive int userID, @RequestBody UserDto updatedUserDto) {

        userService.updateUser(userID, updatedUserDto);

        return ResponseEntity.ok("User updated successfully");
    }

    @PatchMapping("users/password/update")
    public void updateUserPassword(@RequestParam @Positive int userID,
                                   @Valid @RequestBody UserPasswordUpdateRequestDto request) {

        userService.updateUserPassword(userID, request.getOldPassword(),
                request.getNewPassword(), request.getNewPasswordRepeat());
    }

    @PostMapping(value = "/users/login")
    public ResponseEntity<Void> userLogIn(@Valid @RequestBody UserLoginDto loginDTO) {

        int loggedUserID = userService.loginUser(loginDTO.getUserName(), loginDTO.getUserPassword());

        return ResponseEntity.created(URI.create("api/v1/users/login" + loggedUserID)).build();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUserByUserID(@PathVariable int userId) {

        return ResponseEntity.ok(userService.getUserByID(userId));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUserByUserID() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping(value = "/users", params = {"userName"})
    public ResponseEntity<UserDto> getUserByUserName(@RequestParam("userName")
                                                     @NotBlank(message = "UserName cannot be Blank!")
                                                     @NotEmpty(message = "UserName cannot be Empty!")
                                                     @NotNull(message = "UserName cannot be null!")
                                                     String userName) {

        return ResponseEntity.ok(userService.getUserByUserName(userName));
    }

    @GetMapping(value = "/users", params = {"email"})
    public ResponseEntity<UserDto> getUserByUserEmail(@RequestParam("email")
                                                      @NotBlank(message = "UserName cannot be Blank!")
                                                      @NotEmpty(message = "UserName cannot be Empty!")
                                                      @NotNull(message = "UserName cannot be null!")
                                                      String email) {

        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping(value = "/users", params = {"firstName", "lastName"})
    public ResponseEntity<List<UserDto>> getUserByFirstAndLastName(@RequestParam("firstName")
                                                                   @NotBlank(message = "User First Name cannot be Blank!")
                                                                   @NotEmpty(message = "User First Name be Empty!")
                                                                   @NotNull(message = "User First Name be null!")
                                                                   String firstName,
                                                                   @RequestParam("lastName")
                                                                   @NotBlank(message = "User First Name be Blank!")
                                                                   @NotEmpty(message = "User First Name be Empty!")
                                                                   @NotNull(message = "User First Name be null!")
                                                                   String lastName) {

        return ResponseEntity.ok(userService.getUserByFirstAndLastName(firstName, lastName));
    }

    @GetMapping(value = "/users", params = {"firstName"})
    public ResponseEntity<List<UserDto>> getUserByUserFirstName(@RequestParam("firstName")
                                                                @NotBlank(message = "User Name cannot be Blank!")
                                                                @NotEmpty(message = "User Name cannot be Empty!")
                                                                @NotNull(message = "User Name cannot be null!")
                                                                String firstName) {

        return ResponseEntity.ok(userService.getUsersByFirstName(firstName));
    }

    @GetMapping(value = "/users", params = {"lastName"})
    public ResponseEntity<List<UserDto>> getUserByUserLastName(@RequestParam("lastName")
                                                               @NotBlank(message = "User Name cannot be Blank!")
                                                               @NotEmpty(message = "User Name cannot be Empty!")
                                                               @NotNull(message = "User Name cannot be null!")
                                                               String lastName) {

        return ResponseEntity.ok(userService.getUsersByLastName(lastName));
    }

    @DeleteMapping(value = "/users", params = {"userName"})
    public void deleteUserByUserName(@RequestParam("userName")
                                     @NotBlank(message = "UserName cannot be Blank!")
                                     @NotEmpty(message = "UserName cannot be Empty!")
                                     @NotNull(message = "UserName cannot be null!")
                                     String userName) {

        userService.deleteUserByUserName(userName);
    }

    @DeleteMapping(value = "/users", params = {"userId"})
    public void deleteUserByUserID(@RequestParam("userId")
                                   @Positive(message = "User ID must be a Positive number!")
                                   int userID) {

        userService.deleteUserByID(userID);
    }
}
