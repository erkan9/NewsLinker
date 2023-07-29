package erkamber.repositories;

import erkamber.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findUserByUserFirstNameAndUserLastName(String firstName, String lastName);

    Optional<User> findUserByUserName(String userName);

    Optional<User> findUserByUserEmail(String userEmail);

    Optional<User> findUserByUserNameAndUserPassword(String userName, String password);

    List<User> findUserByUserFirstName(String firstName);

    List<User> findUserByUserLastName(String lastName);
}
