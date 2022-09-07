package za.co.ebank.bank.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import za.co.ebank.bank.model.persistence.UserAccount;
import za.co.ebank.bank.repo.UserAccountRepo;

@Service
public class UserAccountService {
    private final UserAccountRepo userAccountRepo;

    public UserAccountService(final UserAccountRepo userAccountRepo) {
        this.userAccountRepo = userAccountRepo;
    }

    public UserAccount createUserAccount(final UserAccount userAccount) {
        
        //create temp password

        //encrypt passwords

        //create user
        return userAccountRepo.save(userAccount);

        //sendEmail with temp password to be changed upon log in

    }
    
    public UserAccount updateUserAccount(final UserAccount userAccount) {      
        return userAccountRepo.save(userAccount);
    }
    
    public Optional<UserAccount> findById(final long id) {        
        return userAccountRepo.findById(id);
    }
}
