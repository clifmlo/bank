package za.co.ebank.bank.service;

import java.net.UnknownHostException;
import javax.mail.MessagingException;
import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import za.co.ebank.bank.exception.UserExistsException;
//import za.co.ebank.bank.mailer.MailSender;
import za.co.ebank.bank.model.dto.SignUpDto;
import za.co.ebank.bank.model.persistence.UserAccount;

@SpringBootTest
@RunWith(SpringRunner.class)  
public class UserAccountServiceTests {
    @Autowired
    UserAccountService userAccountService;
//    @Autowired
//    MailSender mailSender;

    @Test
    public void shouldCreateUserAccount() throws UserExistsException, MessagingException, UnknownHostException {
        SignUpDto signUpDto = SignUpDto.builder()
                                .name("John")
                                .surname("Doe")
                                .idNumber("1999987867083") 
                                .email("test@test.com")
                                .contactNumber("0117654822")
                                .build();
        
        UserAccount userAccount = userAccountService.createUserAccount("http://localhost:8080", signUpDto);
        assertNotNull(userAccount);               
        userAccountService.deleteUserAccount(userAccount.getId()); 
        // verify(mailSender, times(1)).sendHTMLEmail(any());
    }
}
