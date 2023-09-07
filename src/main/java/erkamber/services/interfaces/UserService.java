package erkamber.services.interfaces;

import erkamber.dtos.UserDto;

import java.util.List;

public interface UserService {

    int registerUser(UserDto userDto);

    int loginUser(String userName, String userPassword);

    void updateUser(int userID, UserDto updatedUser);

    void deleteUserByID(int userID);

    void deleteUserByUserName(String userName);

    UserDto getUserByID(int userID);

    UserDto getUserByUserName(String userName);

    UserDto getUserByEmail(String userEmail);

    List<UserDto> getUserByFirstAndLastName(String firstName, String lastName);

    List<UserDto> getUsersByFirstName(String userFirstName);

    List<UserDto> getUsersByLastName(String userLastName);

    List<UserDto> getAllUsers();
}
