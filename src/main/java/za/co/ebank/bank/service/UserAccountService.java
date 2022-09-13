package za.co.ebank.bank.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.co.ebank.bank.exception.UserExistsException;
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

    public UserAccountService(final UserAccountRepo userAccountRepo, final RoleRepo roleRepo, final PasswordEncoder passwordEncoder) {
        this.userAccountRepo = userAccountRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }

    public UserAccount createUserAccount(final SignUpDto signUpDto) throws UserExistsException {
        //check if user exists
        if (userExist(signUpDto.getEmail())) {
            throw new UserExistsException("There is already an account with email: " + signUpDto.getEmail());
        }

        //encrypt password
        final String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        UserAccount userAccount = new UserAccount();
        userAccount.setName(signUpDto.getName());
        userAccount.setSurname(signUpDto.getSurname());
        userAccount.setIdNumber(signUpDto.getIdNumber());
        userAccount.setEmail(signUpDto.getEmail());
        userAccount.setContactNumber(signUpDto.getContactNumber());
        userAccount.setPassword(encodedPassword);
        userAccount.setConfirmPassword(encodedPassword);
        
        Role roles = roleRepo.findByName("USER").get();
        userAccount.setRoles(Collections.singleton(roles));
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
        return !userAccountRepo.findByEmail(email).isEmpty();
    }

    public List<UserAccount> findAll() {
        return userAccountRepo.findAll();
    }

    public Optional<UserAccount> findByEmil(final String email) {
        return userAccountRepo.findByEmail(email);
    }
    
}
