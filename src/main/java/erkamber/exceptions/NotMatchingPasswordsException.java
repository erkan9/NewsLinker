package erkamber.exceptions;

public class NotMatchingPasswords extends RuntimeException {

    public NotMatchingPasswords(String message) {
        super(message);
    }
}
