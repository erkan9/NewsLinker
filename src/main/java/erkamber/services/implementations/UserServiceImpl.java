package erkamber.services.implementations;

import erkamber.configurations.PasswordEncoderConfiguration;
import erkamber.dtos.UserDto;
import erkamber.entities.User;
import erkamber.exceptions.InvalidInputException;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.mappers.UserMapper;
import erkamber.repositories.UserRepository;
import erkamber.services.interfaces.UserService;
import erkamber.validations.UserValidation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final UserValidation userValidation;

    private final PasswordEncoderConfiguration passwordEncoderConfiguration;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserValidation userValidation,
                           PasswordEncoderConfiguration passwordEncoderConfiguration) {

        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userValidation = userValidation;
        this.passwordEncoderConfiguration = passwordEncoderConfiguration;
    }

    @Override
    public int registerUser(UserDto userDto) {

        isNewUserCredentialsValid(userDto);

        String encodedUserPassword = passwordEncoderConfiguration.passwordEncoder().encode(userDto.getUserPassword());

        userDto.setUserPassword(encodedUserPassword);

        User newUser = userMapper.mapUserDtoToUser(userDto);

        userRepository.save(newUser);

        return newUser.getUserID();
    }

    @Override
    public int loginUser(String userName, String userPassword) {

        User searchedUser = getLoginUser(userName, userPassword);

        return searchedUser.getUserID();
    }

    @Override
    public UserDto getUserByID(int userID) {

        Optional<User> searchedUser = userRepository.findById(userID);

        User user = searchedUser.orElseThrow(() ->
                new ResourceNotFoundException("User ID not Found:" + userID, "User"));

        return userMapper.mapUserToUserDto(user);
    }

    @Override
    public UserDto getUserByUserName(String userName) {

        isUserNameValid(userName);

        Optional<User> searchedUser = userRepository.findUserByUserName(userName);

        User user = searchedUser.orElseThrow(() ->
                new ResourceNotFoundException("User not Found:" + userName, "User"));

        return userMapper.mapUserToUserDto(user);
    }

    @Override
    public UserDto getUserByEmail(String userEmail) {

        Optional<User> searchedUser = userRepository.findUserByUserEmail(userEmail);

        User user = searchedUser.orElseThrow(() ->
                new ResourceNotFoundException("User Email not Found:" + userEmail, "User"));

        return userMapper.mapUserToUserDto(user);
    }

    @Override
    public List<UserDto> getUserByFirstAndLastName(String firstName, String lastName) {

        isUserFirstOrLastNameValid(firstName);

        isUserFirstOrLastNameValid(lastName);

        List<User> searchedUsers = userRepository.findUserByUserFirstNameAndUserLastName(firstName, lastName);

        return userMapper.mapUserListToUserDto(searchedUsers);
    }

    @Override
    public List<UserDto> getUsersByFirstName(String userFirstName) {

        isUserFirstOrLastNameValid(userFirstName);

        List<User> searchedUsersByFirstName = userRepository.findUserByUserFirstName(userFirstName);

        return userMapper.mapUserListToUserDto(searchedUsersByFirstName);
    }

    @Override
    public List<UserDto> getUsersByLastName(String userLastName) {

        isUserFirstOrLastNameValid(userLastName);

        List<User> searchedUsersByLastName = userRepository.findUserByUserLastName(userLastName);

        return userMapper.mapUserListToUserDto(searchedUsersByLastName);
    }

    @Override
    public List<UserDto> getAllUsers() {

        List<User> allUsers = userRepository.findAll();

        return userMapper.mapUserListToUserDto(allUsers);
    }

    private void isUserPasswordValid(String userPassword) {

        if (!userValidation.isUserPasswordValid(userPassword)) {

            throw new InvalidInputException("Invalid Password");
        }
    }


    private void isUserFirstOrLastNameValid(String userFirstOrLastName) {

        if (!userValidation.isUserFirstOrLastNameValid(userFirstOrLastName)) {

            throw new InvalidInputException("Invalid Name");
        }
    }

    private void isUserNameValid(String userName) {

        if (!userValidation.isUserNameValid(userName)) {

            throw new InvalidInputException("Invalid Username. It can only contain chars and numbers!");
        }
    }

    protected String getUserNameOfCommentAuthor(int commentAuthorID) {

        Optional<User> commentAuthor = userRepository.findById(commentAuthorID);

        User searchedCommentAuthor = commentAuthor.orElseThrow(() ->
                new ResourceNotFoundException("User not Found:" + commentAuthorID, "User"));

        return searchedCommentAuthor.getUserName();
    }

    private void isNewUserCredentialsValid(UserDto userDto) {

        isUserFirstOrLastNameValid(userDto.getUserFirstName());

        isUserFirstOrLastNameValid(userDto.getUserLastName());

        isUserNameValid(userDto.getUserName());

        isUserPasswordValid(userDto.getUserPassword());
    }

    private User getLoginUser(String userName, String password) {

        List<User> allUsers = userRepository.findAll();

        return allUsers.stream()
                .filter(user -> user.getUserName().equals(userName) &&
                        passwordEncoderConfiguration.passwordEncoder().matches(password, user.getUserPassword()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userName, "User"));
    }
}
