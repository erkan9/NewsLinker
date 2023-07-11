package erkamber.services.interfaces;

import javax.mail.MessagingException;

public interface EmailService {

    void sendEmail(String newsAuthorName, String commentAuthorName, String comment, String authorEmail, String postTitle) throws MessagingException;
}
