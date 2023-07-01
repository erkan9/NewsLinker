package erkamber.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, updatable = false, insertable = false, nullable = false)
    private int userID;

    @Column(name = "user_first_name", length = 20, unique = false, updatable = true, insertable = true, nullable = false)
    private String userFirstName;

    @Column(name = "user_last_name", length = 20, unique = false, updatable = true, insertable = true, nullable = false)
    private String userLastName;

    @Column(name = "user_name", length = 20, unique = true, updatable = false, insertable = true, nullable = false)
    private String userName;

    @Column(name = "user_email", length = 30, unique = true, updatable = false, insertable = true, nullable = false)
    private String userEmail;

    @Column(name = "user_password", length = 35, unique = false, updatable = true, insertable = true, nullable = false)
    private String userPassword;

    @Column(name = "user_photo", length = 255, unique = false, updatable = true, insertable = true, nullable = true)
    private String userPhoto;

    public User() {
    }

    public User(int userID, String userName, String userEmail, String userPassword) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public User(int userID, String userName, String userEmail, String userPassword, String userPhoto) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhoto = userPhoto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userID == user.userID && Objects.equals(userFirstName, user.userFirstName) &&
                Objects.equals(userLastName, user.userLastName) && Objects.equals(userName, user.userName) &&
                Objects.equals(userEmail, user.userEmail) && Objects.equals(userPassword, user.userPassword) &&
                Objects.equals(userPhoto, user.userPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, userFirstName, userLastName, userName, userEmail, userPassword, userPhoto);
    }
}
