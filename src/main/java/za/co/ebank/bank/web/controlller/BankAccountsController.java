package za.co.ebank.bank.web.controlller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import za.co.ebank.bank.model.persistence.BankAccount;
import za.co.ebank.bank.model.dto.CreateBankAccount;
import za.co.ebank.bank.model.dto.UserBankAccount;
import za.co.ebank.bank.service.BankAccountService;

@RequestMapping("/bank-account")
@RestController
public class BankAccountsController {
    private final BankAccountService bankAccountService;


    public BankAccountsController(final BankAccountService bankAccountService) {
      this.bankAccountService = bankAccountService;
    }

    @PostMapping("create")
    public ResponseEntity createBankAccount(@RequestBody final CreateBankAccount bankAccount) {
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
    
    @PostMapping("/link-user")
    public ResponseEntity linkUser(@RequestBody final UserBankAccount userBankAccount) {
        BankAccount bankAccount = bankAccountService.findByAccountNumber(userBankAccount.getAccountNumber());
        bankAccount.setUser_account_id(userBankAccount.getUserId());
        bankAccountService.updateBankAccount(bankAccount);
        
        return new ResponseEntity(bankAccount, HttpStatus.OK);            
    }
}