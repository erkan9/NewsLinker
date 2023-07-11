package erkamber.services.implementations;

import erkamber.services.interfaces.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final ITemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender javaMailSender, ITemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(String newsAuthorName, String commentAuthorName, String comment, String authorEmail, String postTitle) throws MessagingException {

        Context context = new Context();

        context.setVariable("newsAuthorName", newsAuthorName);
        context.setVariable("commentAuthorName", commentAuthorName);
        context.setVariable("comment", comment);
        context.setVariable("postTitle", postTitle);

        String process = templateEngine.process("/email", context);

        MimeMessageHelper helper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);

        helper.setTo(authorEmail);
        helper.setSubject("New Comment Left");
        helper.setText(process, true);

        javaMailSender.send(helper.getMimeMessage());
    }
}
