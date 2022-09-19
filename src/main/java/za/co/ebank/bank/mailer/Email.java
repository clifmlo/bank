package za.co.ebank.bank.mailer;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author cliff
 */
@Data
@AllArgsConstructor
public class Email {    
    private String to;
    private String from;
    private String subject;
    private String content;
    private String template;
    private String contentType;
    
    public Email() {
        this.contentType = "text/html";
    }
}
