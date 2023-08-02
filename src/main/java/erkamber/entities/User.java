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

    @Column(name = "user_password", unique = false, updatable = true, insertable = true, nullable = false)
    private String userPassword;

    @Column(name = "user_photo", length = 255, unique = false, updatable = true, insertable = true, nullable = true)
    private String userPhoto;

    @Column(name = "user_is_reporter", unique = false, updatable = false, insertable = true, nullable = false)
    private boolean userReporter = false;

    public User() {
    }

    public User(int userID, String userFirstName, String userLastName, String userName, String userEmail,
                String userPassword, boolean userReporter) {

        this.userID = userID;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userReporter = userReporter;
    }

    public User(int userID, String userFirstName, String userLastName, String userName, String userEmail,
                String userPassword, String userPhoto, boolean userReporter) {

        this.userID = userID;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhoto = userPhoto;
        this.userReporter = userReporter;
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
