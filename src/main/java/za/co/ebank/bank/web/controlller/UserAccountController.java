package za.co.ebank.bank.web.controlller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import za.co.ebank.bank.model.persistence.UserAccount;
import za.co.ebank.bank.service.UserAccountService;

@RequestMapping("/api/v1/user") 
@RestController
@CrossOrigin(origins = {"http://localhost:4200/"}, methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class UserAccountController {
    private final UserAccountService userAccountService;

    public UserAccountController(final UserAccountService userAccountService) {
       this.userAccountService = userAccountService;
    }
    
    @GetMapping("{id}")
    public ResponseEntity findUserAccount(@PathVariable final long id) {
        Optional<UserAccount> userAccount = userAccountService.findById(id);
        
        if (userAccount.isPresent()) {
           return new ResponseEntity(userAccount, HttpStatus.OK);
        }
        
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("all")
    public ResponseEntity findUserAllUsers() {
        List<UserAccount> users = userAccountService.findAll();
        return new ResponseEntity(users, HttpStatus.OK);
    }
    
    @PostMapping("update")
    public ResponseEntity updateUserAccount(@RequestBody final UserAccount userAccount) {
        UserAccount updatedUserAccount = userAccountService.updateUserAccount(userAccount);
        
        return new ResponseEntity(updatedUserAccount, HttpStatus.OK);
    }
    
    @DeleteMapping("delete/{id}")
    public ResponseEntity deleteUserAccount(@PathVariable final long id) {
        userAccountService.deleteUserAccount(id);        
        return new ResponseEntity(HttpStatus.OK);
    }
}
