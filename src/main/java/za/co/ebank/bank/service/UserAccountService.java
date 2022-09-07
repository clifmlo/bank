package za.co.ebank.bank.service;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.co.ebank.bank.exception.UserExistsException;
import za.co.ebank.bank.model.persistence.UserAccount;
import za.co.ebank.bank.repo.UserAccountRepo;

@Service
@Slf4j
public class UserAccountService {
    private final UserAccountRepo userAccountRepo;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(final UserAccountRepo userAccountRepo, final PasswordEncoder passwordEncoder) {
        this.userAccountRepo = userAccountRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccount createUserAccount(final UserAccount userAccount) throws UserExistsException {
        //check if user exists
        if (userExist(userAccount.getEmail())) {
            throw new UserExistsException("There is already an account with email: " + userAccount.getEmail());
        }

        //encrypt password
        final String encodedPassword = passwordEncoder.encode(userAccount.getPassword());        
        userAccount.setPassword(encodedPassword);
        userAccount.setConfirmPassword(encodedPassword);
        return userAccountRepo.save(userAccount);

        //sendEmail with temp password to be changed upon log in        
    }
    
    public UserAccount updateUserAccount(final UserAccount userAccount) {      
        return userAccountRepo.save(userAccount);
    }
    
    public Optional<UserAccount> findById(final long id) {        
        return userAccountRepo.findById(id);
    }
    
    private boolean userExist(final String email) {
        return userAccountRepo.findByEmail(email) != null;
    }
    
}
