package erkamber.services.interfaces;

import erkamber.dtos.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserByID(int userID);

    UserDto getUserByUserName(String userName);

    UserDto getUserByEmail(String userEmail);

    UserDto getUserByFirstAndLastName(String firstName, String lastName);

    List<UserDto> getUsersByFirstName(String userFirstName);

    List<UserDto> getUsersByLastName(String userLastName);

    List<UserDto> getAllUsers();
}
