package erkamber.controllers;

import erkamber.configurations.HttpHeadersConfiguration;
import erkamber.dtos.UserDto;
import erkamber.dtos.UserLoginDto;
import erkamber.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
public class UserController {

    private final UserService userService;

    private final HttpHeadersConfiguration httpHeadersConfiguration;

    public UserController(UserService userService, HttpHeadersConfiguration httpHeadersConfiguration) {
        this.userService = userService;
        this.httpHeadersConfiguration = httpHeadersConfiguration;
    }

    @PostMapping("/users/register")
    public ResponseEntity<Void> addNewUser(@Valid @RequestBody UserDto userDto) {

        int newUserID = userService.registerUser(userDto);

        httpHeadersConfiguration.getHeaders().set("RegisteredUserID", String.valueOf(newUserID));

        return new ResponseEntity<>(httpHeadersConfiguration.getHeaders(), HttpStatus.CREATED);
    }

    @PostMapping(value = "/users/login")
    public ResponseEntity<Void> userLogIn(@Valid @RequestBody UserLoginDto loginDTO) {

        int newUserID = userService.loginUser(loginDTO.getUserName(), loginDTO.getUserPassword());

        httpHeadersConfiguration.getHeaders().set("LoggedUserID", String.valueOf(newUserID));

        return new ResponseEntity<>(httpHeadersConfiguration.getHeaders(), HttpStatus.FOUND);
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
}
