package erkamber.services.implementations;

import erkamber.configurations.PasswordEncoderConfiguration;
import erkamber.dtos.UserDto;
import erkamber.entities.User;
import erkamber.exceptions.InvalidInputException;
import erkamber.exceptions.NotMatchingPasswordsException;
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
     * Logs in a user with the provided user email and password.
     *
     * @param userEmail     The email of the user.
     * @param userPassword The password of the user.
     * @return The ID of the logged-in user.
     * @throws ResourceNotFoundException If the provided login credentials are invalid.
     */
    @Override
    public int loginUser(String userEmail, String userPassword) {

        User searchedUser = getLoginUser(userEmail, userPassword);

        return searchedUser.getUserID();
    }

    /**
     * Updates a user's information, such as First/Last name, password and photo based on the provided UserDto.
     *
     * @param userID      The ID of the user to update.
     * @param updatedUser The UserDto containing the updated user information.
     * @throws ResourceNotFoundException If no user with the specified userID is found.
     */
    @Override
    public void updateUser(int userID, UserDto updatedUser) {

        Optional<User> searchedUser = userRepository.findById(userID);

        User userToUpdate = searchedUser.orElseThrow(() ->
                new ResourceNotFoundException("User ID not Found:" + userID, "User"));

        // Check and update the user's first name
        if (updatedUser.getUserFirstName() != null) {
            userToUpdate.setUserFirstName(updatedUser.getUserFirstName());
        }

        // Check and update the user's last name
        if (updatedUser.getUserLastName() != null) {
            userToUpdate.setUserLastName(updatedUser.getUserLastName());
        }

        // Check and update the user's photo
        if (updatedUser.getUserPhoto() != null) {
            userToUpdate.setUserPhoto(updatedUser.getUserPhoto());
        }

        userRepository.save(userToUpdate);
    }

    /**
     * Updates the user's password.
     * <p>
     * This method allows a user to update their password by providing the old password
     * for verification and specifying the new password.
     *
     * @param userID            The unique identifier of the user.
     * @param oldPassword       The user's old password for verification.
     * @param newPassword       The user's new password.
     * @param newPasswordRepeat The repeated new password for confirmation.
     * @throws ResourceNotFoundException If the user with the specified userID is not found.
     * @throws NotMatchingPasswordsException      If the old password does not match the stored password or
     *                                   if the new passwords provided do not match.
     */
    @Override
    public void updateUserPassword(int userID, String oldPassword, String newPassword, String newPasswordRepeat) {

        isUserPasswordValid(newPassword);

        Optional<User> searchedUserOptional = userRepository.findById(userID);

        User searchedUser = searchedUserOptional.orElseThrow(() ->
                new ResourceNotFoundException("User ID not Found:" + userID, "User"));

        String oldPasswordEncoded = searchedUser.getUserPassword();

        areOldPasswordsMatching(oldPassword, oldPasswordEncoded);

        areNewPasswordsMatching(newPassword, newPasswordRepeat);

        searchedUser.setUserPassword(passwordEncoderConfiguration.passwordEncoder().encode(newPassword));

        userRepository.save(searchedUser);
    }

    /**
     * Checks if the provided old password matches the encoded old password from the DB.
     * <p>
     * This method is used to validate whether the provided old password matches
     * the encoded old password stored in the user's data.
     *
     * @param oldPassword        The user's old password to be validated.
     * @param oldPasswordEncoded The stored encoded old password to be compared with.
     * @throws NotMatchingPasswordsException If the provided old password does not match the encoded old password.
     */
    private void areOldPasswordsMatching(String oldPassword, String oldPasswordEncoded) {

        if (!userValidation.areOldPasswordMatching(oldPassword, oldPasswordEncoded)) {

            throw new NotMatchingPasswordsException("Old User passwords are not Matching!");
        }
    }

    /**
     * Checks if two new passwords match.
     * <p>
     * This method is used to validate whether the provided new password and
     * its repetition match each other.
     *
     * @param newPassword       The new password to be validated.
     * @param newPasswordRepeat The repeated entry of the new password to be validated.
     * @throws NotMatchingPasswordsException If the provided new passwords do not match.
     */
    private void areNewPasswordsMatching(String newPassword, String newPasswordRepeat) {

        if (!userValidation.areNewPasswordsMatching(newPassword, newPasswordRepeat)) {

            throw new NotMatchingPasswordsException("New User passwords are not Matching!");
        }
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
     * Validates if user's userEmail is valid
     *
     * @param userName the userEmail of the user
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
     * Method used to find and return the user trying to log in by userEmail and password
     *
     * @param userEmail the userEmail of the user trying to log in
     * @param password the password of the user trying to log in
     * @return the user as object trying to log in
     */
    private User getLoginUser(String userEmail, String password) {

        List<User> allUsers = userRepository.findAll();

        return allUsers.stream()
                .filter(user -> user.getUserEmail().equals(userEmail) &&
                        passwordEncoderConfiguration.passwordEncoder().matches(password, user.getUserPassword()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Incorrect email or password!", "User"));
    }
}
