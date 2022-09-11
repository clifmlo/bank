package za.co.ebank.bank.web.controlller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import za.co.ebank.bank.model.persistence.BankAccount;
import za.co.ebank.bank.model.dto.CreateBankAccount;
import za.co.ebank.bank.model.dto.UserBankAccount;
import za.co.ebank.bank.service.BankAccountService;

@Slf4j
@RequestMapping("/api/v1/bank-account")
@RestController
@CrossOrigin(origins = {"http://localhost:4200/"}, methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.OPTIONS})
public class BankAccountsController {
    private final BankAccountService bankAccountService;


    public BankAccountsController(final BankAccountService bankAccountService) {
      this.bankAccountService = bankAccountService;
    }

    @PostMapping("create")
    public ResponseEntity createBankAccount(@RequestBody final CreateBankAccount bankAccount) {
        log.info(bankAccount.toString());
        BankAccount createdBankAccount = bankAccountService.createBankAccount(bankAccount);        
        return createdBankAccount.getId() > 0 ? new ResponseEntity(createdBankAccount, HttpStatus.CREATED) : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @GetMapping("{accountNumber}")
    public ResponseEntity findBankAccount(@PathVariable final String accountNumber) {
        BankAccount bankAccount = bankAccountService.findByAccountNumber(accountNumber);
        
        if (bankAccount != null) {
           return new ResponseEntity(bankAccount, HttpStatus.OK);
        }
        
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("user/{userId}")
    public ResponseEntity findBankAccountsByUserId(@PathVariable final long userId) {
       // log.info(userId);
        List<BankAccount> bankAccounts = bankAccountService.findBankAccountsByUserId(userId);
        
        if (bankAccounts != null) {
           return new ResponseEntity(bankAccounts, HttpStatus.OK);
        }
        
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    
    @PostMapping("/link-user")
    public ResponseEntity linkUser(@RequestBody final UserBankAccount userBankAccount) {
        BankAccount bankAccount = bankAccountService.findByAccountNumber(userBankAccount.getAccountNumber());
        bankAccount.setUser_account_id(userBankAccount.getUserId());
        bankAccountService.updateBankAccount(bankAccount);
        
        return new ResponseEntity(bankAccount, HttpStatus.OK);            
    }
}
