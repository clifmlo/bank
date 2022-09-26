
package za.co.ebank.bank.service;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.co.ebank.bank.builders.MailBuilder;
import za.co.ebank.bank.exception.PasswordMissmatchException;
import za.co.ebank.bank.exception.UserExistsException;
import za.co.ebank.bank.mailer.Email;
import za.co.ebank.bank.mailer.MailSender;
import za.co.ebank.bank.model.dto.SignUpDto;
import za.co.ebank.bank.model.persistence.Role;
import za.co.ebank.bank.model.persistence.UserAccount;
import za.co.ebank.bank.repo.RoleRepo;
import za.co.ebank.bank.repo.UserAccountRepo;

@Service
@Slf4j
public class UserAccountService {
    private final UserAccountRepo userAccountRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    
    @Value("${spring.mail.from}")
    private String mailFrom;

    public UserAccountService(final UserAccountRepo userAccountRepo, final RoleRepo roleRepo, final PasswordEncoder passwordEncoder, final MailSender mailSender) {
        this.userAccountRepo = userAccountRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.mailSender = mailSender;
    }

    public UserAccount createUserAccount(final String loginLink, final SignUpDto signUpDto) throws UserExistsException, MessagingException, UnknownHostException {
        //check if user exists
        if (userExist(signUpDto.getEmail())) {
            throw new UserExistsException("There is already an account with email: " + signUpDto.getEmail());
        }              

        //encrypt password
        final String geneRatedPassword = generateRandomPassword();
        final String encodedPassword = passwordEncoder.encode(geneRatedPassword);
        UserAccount userAccount = new UserAccount();
        userAccount.setName(signUpDto.getName());
        userAccount.setSurname(signUpDto.getSurname());
        userAccount.setIdNumber(signUpDto.getIdNumber());
        userAccount.setEmail(signUpDto.getEmail());
        userAccount.setContactNumber(signUpDto.getContactNumber());
        userAccount.setPassword(encodedPassword);
        userAccount.setConfirmPassword(encodedPassword);
        userAccount.setActive(false);
        
        Role roles = roleRepo.findByName("USER").get();
        userAccount.setRoles(Collections.singleton(roles));
        userAccountRepo.save(userAccount);
                
        sendRegistrationMail(userAccount, geneRatedPassword, loginLink);

        return userAccount;        
    }
    
    public UserAccount updateUserAccount(final UserAccount userAccount) {      
        return userAccountRepo.save(userAccount);
    }
    
    public Optional<UserAccount> findById(final long id) {        
        return userAccountRepo.findById(id);
    }
    
    private boolean userExist(final String email) {        
        return !userAccountRepo.findByEmail(email).isEmpty();
    }

    public List<UserAccount> findAll() {
        return userAccountRepo.findAll();
    }

    public Optional<UserAccount> findByEmil(final String email) {
        return userAccountRepo.findByEmail(email);
    } 
    
    private void sendRegistrationMail(final UserAccount userAccount, final String password, final String loginLink) throws MessagingException, UnknownHostException {        
        final Email mail = new MailBuilder()
                            .from(this.mailFrom) 
                            .to(userAccount.getEmail())
                            .template("email.html")
                            .addContext("name", userAccount.getName())
                            .addContext("email", userAccount.getEmail())
                            .addContext("pass", password)
                            .addContext("link", loginLink)
                            .subject("Welcome to eBank")
                            .createMail();
       
        this.mailSender.sendHTMLEmail(mail);
    }
    
    public String generateRandomPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            @Override
            public String getErrorCode() {
                return "";
            }
            @Override
            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(10, splCharRule, lowerCaseRule, upperCaseRule, digitRule);
        
        return password;
    }

    public void deleteUserAccount(final long id) {
        userAccountRepo.deleteById(id);
    }

    public void updatePassword(final String password, final String confirmPassword, final long id) throws PasswordMissmatchException {
        final String encodedPassword = passwordEncoder.encode(password);        
        UserAccount account = userAccountRepo.findById(id).get();
        if (passwordsMatch(password, confirmPassword)) {
            account.setPassword(encodedPassword);
            account.setConfirmPassword(encodedPassword);
            account.setActive(true);
        } else {
           throw new PasswordMissmatchException("Passwords do not match!");
        }
        userAccountRepo.save(account);
    }
    
    private boolean passwordsMatch(final String pass1, final String pass2){
        return pass1.equals(pass2);
    }
}
