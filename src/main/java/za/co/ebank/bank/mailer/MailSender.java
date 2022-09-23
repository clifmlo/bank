package za.co.ebank.bank.mailer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j; 
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 *
 * @author cliff
 */
@Slf4j
@Service    
public class MailSender {
	
    private final JavaMailSender mailSender;

    public MailSender(final JavaMailSender javamailSender) {
        this.mailSender = javamailSender;
    }

    public void sendHTMLEmail(Email message) throws MessagingException {
        MimeMessage emailMessage = mailSender.createMimeMessage();
        MimeMessageHelper mailBuilder = new MimeMessageHelper(emailMessage, true);
        mailBuilder.setTo(message.getTo());
        mailBuilder.setFrom(message.getFrom());			
        mailBuilder.setText(message.getContent(), true);
        mailBuilder.setSubject(message.getSubject());
        mailSender.send(emailMessage);
    }
}