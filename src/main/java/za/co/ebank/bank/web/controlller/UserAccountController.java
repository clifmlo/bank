package za.co.ebank.bank.web.controlller;

import java.util.Optional;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.ebank.bank.model.ApiResponse;
import za.co.ebank.bank.model.persistence.UserAccount;
import za.co.ebank.bank.service.UserAccountService;

@RequestMapping("/user")
@RestController
public class UserAccountController {
    private final UserAccountService userAccountService;

    public UserAccountController(final UserAccountService userAccountService) {
       this.userAccountService = userAccountService;
    }

    @PostMapping("register")
    public ResponseEntity createUserAccount(@Valid @RequestBody final UserAccount userAccount) {
        UserAccount createdUserAccount = userAccountService.createUserAccount(userAccount);
        
        return new ResponseEntity<>(new ApiResponse(createdUserAccount, "success", false), HttpStatus.OK);
    }
    
    @GetMapping("{id}")
    public ResponseEntity findUserAccount(@PathVariable final long id) {
        Optional<UserAccount> userAccount = userAccountService.findById(id);
        
        if (userAccount.isPresent()) {
           return new ResponseEntity(userAccount, HttpStatus.OK);
        }
        
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    
    @PostMapping("update")
    public ResponseEntity updateUserAccount(@RequestBody final UserAccount userAccount) {
        UserAccount updatedUserAccount = userAccountService.updateUserAccount(userAccount);
        
        return new ResponseEntity(updatedUserAccount, HttpStatus.OK);
    }
}