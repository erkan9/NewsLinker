package erkamber.services.implementations;

import erkamber.dtos.UserDto;
import erkamber.entities.User;
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

    //TODO DO validations for user inputs using userValidation class
    private final UserValidation userValidation;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserValidation userValidation) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userValidation = userValidation;
    }

    @Override
    public UserDto getUserByID(int userID) {

        Optional<User> searchedUser = userRepository.findById(userID);

        User user = searchedUser.orElseThrow(() ->
                new ResourceNotFoundException("User not Found:" + userID, "User"));

        return userMapper.mapUserToUserDto(user);
    }

    @Override
    public UserDto getUserByUserName(String userName) {

        Optional<User> searchedUser = userRepository.findUserByUserName(userName);

        User user = searchedUser.orElseThrow(() ->
                new ResourceNotFoundException("User not Found:" + userName, "User"));

        return userMapper.mapUserToUserDto(user);
    }

    @Override
    public UserDto getUserByEmail(String userEmail) {

        Optional<User> searchedUser = userRepository.findUserByUserEmail(userEmail);

        User user = searchedUser.orElseThrow(() ->
                new ResourceNotFoundException("User not Found:" + userEmail, "User"));

        return userMapper.mapUserToUserDto(user);
    }

    @Override
    public UserDto getUserByFirstAndLastName(String firstName, String lastName) {

        Optional<User> searchedUser = userRepository.findUserByUserFirstNameAndUserLastName(firstName, lastName);

        User user = searchedUser.orElseThrow(() ->
                new ResourceNotFoundException("User not Found:" + firstName + " " + lastName, "User"));

        return userMapper.mapUserToUserDto(user);
    }

    @Override
    public List<UserDto> getUsersByFirstName(String userFirstName) {

        List<User> searchedUsersByFirstName = userRepository.findUserByUserFirstName(userFirstName);

        return userMapper.mapUserListToUserDto(searchedUsersByFirstName);
    }

    @Override
    public List<UserDto> getUsersByLastName(String userLastName) {

        List<User> searchedUsersByLastName = userRepository.findUserByUserLastName(userLastName);

        return userMapper.mapUserListToUserDto(searchedUsersByLastName);
    }

    @Override
    public List<UserDto> getAllUsers() {

        List<User> allUsers = userRepository.findAll();

        return userMapper.mapUserListToUserDto(allUsers);
    }
}
