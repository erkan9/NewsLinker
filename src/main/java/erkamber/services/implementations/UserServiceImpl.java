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

    /**
     * Registers a new user with the provided user information.
     *
     * @param userDto The user information to be registered.
     * @return The ID of the newly registered user.
     * @throws InvalidInputException If the provided user credentials are invalid.
     */
    @Override
    public int registerUser(UserDto userDto) {

        isNewUserCredentialsValid(userDto);

        String encodedUserPassword = passwordEncoderConfiguration.passwordEncoder().encode(userDto.getUserPassword());

        userDto.setUserPassword(encodedUserPassword);

        User newUser = userMapper.mapUserDtoToUser(userDto);

        userRepository.save(newUser);

        return newUser.getUserID();
    }

    /**
     * Logs in a user with the provided username and password.
     *
     * @param userName     The username of the user.
     * @param userPassword The password of the user.
     * @return The ID of the logged-in user.
     * @throws ResourceNotFoundException If the provided login credentials are invalid.
     */
    @Override
    public int loginUser(String userName, String userPassword) {

        User searchedUser = getLoginUser(userName, userPassword);

        return searchedUser.getUserID();
    }

    /**
     * Deletes a user by their user ID.
     *
     * @param userID The ID of the user to be deleted.
     * @throws ResourceNotFoundException If the user with the given ID is not found.
     */
    @Override
    public void deleteUserByID(int userID) {

        Optional<User> searchedUser = userRepository.findById(userID);

        User userToDelete = searchedUser.orElseThrow(() ->
                new ResourceNotFoundException("User ID not Found:" + userID, "User"));

        userRepository.delete(userToDelete);
    }

    /**
     * Deletes a user by their username.
     *
     * @param userName The username of the user to be deleted.
     * @throws ResourceNotFoundException If the user with the given username is not found.
     */
    @Override
    public void deleteUserByUserName(String userName) {

        Optional<User> searchedUser = userRepository.findUserByUserName(userName);

        User userToDelete = searchedUser.orElseThrow(() ->
                new ResourceNotFoundException("User ID not Found:" + userName, "User"));

        userRepository.delete(userToDelete);
    }

    /**
     * Retrieves a user's information by their user ID.
     *
     * @param userID The ID of the user to retrieve.
     * @return The UserDto representing the user's information.
     * @throws ResourceNotFoundException If the user with the given ID is not found.
     */
    @Override
    public UserDto getUserByID(int userID) {

        Optional<User> searchedUser = userRepository.findById(userID);

        User user = searchedUser.orElseThrow(() ->
                new ResourceNotFoundException("User ID not Found:" + userID, "User"));

        return userMapper.mapUserToUserDto(user);
    }

    /**
     * Retrieves a user's information by their username.
     *
     * @param userName The username of the user to retrieve.
     * @return The UserDto representing the user's information.
     * @throws InvalidInputException     If the provided username is invalid.
     * @throws ResourceNotFoundException If the user with the given username is not found.
     */
    @Override
    public UserDto getUserByUserName(String userName) {

        isUserNameValid(userName);

        Optional<User> searchedUser = userRepository.findUserByUserName(userName);

        User user = searchedUser.orElseThrow(() ->
                new ResourceNotFoundException("User not Found:" + userName, "User"));

        return userMapper.mapUserToUserDto(user);
    }

    /**
     * Retrieves a user's information by their email address.
     *
     * @param userEmail The email address of the user to retrieve.
     * @return The UserDto representing the user's information.
     * @throws ResourceNotFoundException If the user with the given email address is not found.
     */
    @Override
    public UserDto getUserByEmail(String userEmail) {

        Optional<User> searchedUser = userRepository.findUserByUserEmail(userEmail);

        User user = searchedUser.orElseThrow(() ->
                new ResourceNotFoundException("User Email not Found:" + userEmail, "User"));

        return userMapper.mapUserToUserDto(user);
    }

    /**
     * Retrieves a list of users' information by their first and last names.
     *
     * @param firstName The first name of the users to retrieve.
     * @param lastName  The last name of the users to retrieve.
     * @return A list of UserDto objects representing the users' information.
     * @throws InvalidInputException     If either the first name or last name is invalid.
     * @throws ResourceNotFoundException If no users with the given first and last names are found.
     */
    @Override
    public List<UserDto> getUserByFirstAndLastName(String firstName, String lastName) {

        isUserFirstOrLastNameValid(firstName);

        isUserFirstOrLastNameValid(lastName);

        List<User> searchedUsers = userRepository.findUserByUserFirstNameAndUserLastName(firstName, lastName);

        isUserListEmpty(searchedUsers);

        return userMapper.mapUserListToUserDto(searchedUsers);
    }

    /**
     * Retrieves a list of users' information by their first name.
     *
     * @param userFirstName The first name of the users to retrieve.
     * @return A list of UserDto objects representing the users' information.
     * @throws InvalidInputException     If the first name is invalid.
     * @throws ResourceNotFoundException If no users with the given first name are found.
     */
    @Override
    public List<UserDto> getUsersByFirstName(String userFirstName) {

        isUserFirstOrLastNameValid(userFirstName);

        List<User> searchedUsersByFirstName = userRepository.findUserByUserFirstName(userFirstName);

        isUserListEmpty(searchedUsersByFirstName);

        return userMapper.mapUserListToUserDto(searchedUsersByFirstName);
    }

    /**
     * Retrieves a list of users' information by their last name.
     *
     * @param userLastName The last name of the users to retrieve.
     * @return A list of UserDto objects representing the users' information.
     * @throws InvalidInputException     If the last name is invalid.
     * @throws ResourceNotFoundException If no users with the given last name are found.
     */
    @Override
    public List<UserDto> getUsersByLastName(String userLastName) {

        isUserFirstOrLastNameValid(userLastName);

        List<User> searchedUsersByLastName = userRepository.findUserByUserLastName(userLastName);

        isUserListEmpty(searchedUsersByLastName);

        return userMapper.mapUserListToUserDto(searchedUsersByLastName);
    }

    /**
     * Retrieves a list of all users' information.
     *
     * @return A list of UserDto objects representing the information of all users.
     * @throws ResourceNotFoundException If no users are found.
     */
    @Override
    public List<UserDto> getAllUsers() {

        List<User> allUsers = userRepository.findAll();

        return userMapper.mapUserListToUserDto(allUsers);
    }

    /**
     * Validates if the provided user password is valid.
     *
     * @param userPassword The user password to be validated.
     * @throws InvalidInputException If the user password is invalid.
     */
    private void isUserPasswordValid(String userPassword) {

        if (!userValidation.isUserPasswordValid(userPassword)) {

            throw new InvalidInputException("Invalid Password");
        }
    }

    /**
     * Validates if the provided user first or last name is valid.
     *
     * @param userFirstOrLastName The user first or last name to be validated.
     * @throws InvalidInputException If the user first or last name is invalid.
     */
    private void isUserFirstOrLastNameValid(String userFirstOrLastName) {

        if (!userValidation.isUserFirstOrLastNameValid(userFirstOrLastName)) {

            throw new InvalidInputException("Invalid Name");
        }
    }

    /**
     * Validates if User List is empty
     *
     * @param userList list of users
     * @throws ResourceNotFoundException If user list os empty
     */
    private void isUserListEmpty(List<User> userList) {

        if (userValidation.isListEmpty(userList)) {

            throw new ResourceNotFoundException("Users not Found", "User");
        }
    }

    /**
     * Validates if user's userName is valid
     *
     * @param userName the userName of the user
     */
    private void isUserNameValid(String userName) {

        if (!userValidation.isUserNameValid(userName)) {

            throw new InvalidInputException("Invalid Username. It can only contain chars and numbers!");
        }
    }

    /**
     * Retrieve username of the Author of the comment
     *
     * @param commentAuthorID the ID of the comment's author
     * @return the username of comment's Author
     */
    protected String getUserNameOfCommentAuthor(int commentAuthorID) {

        Optional<User> commentAuthor = userRepository.findById(commentAuthorID);

        User searchedCommentAuthor = commentAuthor.orElseThrow(() ->
                new ResourceNotFoundException("User not Found:" + commentAuthorID, "User"));

        return searchedCommentAuthor.getUserName();
    }

    /**
     * Validates of new user credentials are valid, such as first/last name, username and password
     *
     * @param userDto The new User as DTO
     */
    private void isNewUserCredentialsValid(UserDto userDto) {

        isUserFirstOrLastNameValid(userDto.getUserFirstName());

        isUserFirstOrLastNameValid(userDto.getUserLastName());

        isUserNameValid(userDto.getUserName());

        isUserPasswordValid(userDto.getUserPassword());
    }

    /**
     * Method used to find and return the user trying to log in by userName and password
     *
     * @param userName the userName of the user trying to log in
     * @param password the password of the user trying to log in
     * @return the user as object trying to log in
     */
    private User getLoginUser(String userName, String password) {

        List<User> allUsers = userRepository.findAll();

        return allUsers.stream()
                .filter(user -> user.getUserName().equals(userName) &&
                        passwordEncoderConfiguration.passwordEncoder().matches(password, user.getUserPassword()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userName, "User"));
    }
}
