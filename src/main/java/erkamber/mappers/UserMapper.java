package erkamber.mappers;


import erkamber.dtos.UserDto;
import erkamber.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto mapUserToUserDto(User user) {

        return new UserDto(user.getUserID(), user.getUserFirstName(), user.getUserLastName(), user.getUserName(),
                user.getUserEmail(), user.getUserPassword(), user.getUserPhoto(), user.isUserReporter());
    }

    public User mapUserDtoToUser(UserDto userDto) {

        return new User(userDto.getUserID(), userDto.getUserFirstName(), userDto.getUserLastName(), userDto.getUserName(),
                userDto.getUserEmail(), userDto.getUserPassword(), userDto.getUserPhoto(), userDto.isUserReporter());
    }

    public List<UserDto> mapUserListToUserDto(List<User> listOfUsers) {

        return listOfUsers.stream().map(this::mapUserToUserDto).collect(Collectors.toList());
    }
}
