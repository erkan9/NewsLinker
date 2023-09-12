package erkamber.services.implementations;

import erkamber.services.interfaces.EmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final ITemplateEngine templateEngine;

    private final Logger logger = LogManager.getLogger(EmailServiceImpl.class);

    public EmailServiceImpl(JavaMailSender javaMailSender, ITemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Sends an email notification to a specified recipient regarding a new comment left on a post.
     *
     * @param newsAuthorName    The name of the news article's author.
     * @param commentAuthorName The name of the comment's author.
     * @param comment           The content of the comment.
     * @param authorEmail       The email address of the recipient.
     * @param postTitle         The title of the news article or post.
     */
    @Override
    public void sendEmail(String newsAuthorName, String commentAuthorName, String comment, String authorEmail, String postTitle) {

        try {
            // Prepare the email content using a template engine
            Context context = new Context();
            context.setVariable("newsAuthorName", newsAuthorName);
            context.setVariable("commentAuthorName", commentAuthorName);
            context.setVariable("comment", comment);
            context.setVariable("postTitle", postTitle);

            String process = templateEngine.process("email", context);

            // Create and send the email message
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setTo(authorEmail);
            helper.setSubject("New Comment Left");
            helper.setText(process, true);

            javaMailSender.send(message);
        } catch (Exception exception) {

            logger.error("EMAIL ERROR " + exception.getMessage());
        }
    }
}
