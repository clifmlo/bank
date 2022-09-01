package za.co.ebank.bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import za.co.ebank.bank.model.BankAccount;
import za.co.ebank.bank.service.BankAccountService;

@RequestMapping("/bank-account")
@RestController
public class BankAccountsController {
    private final BankAccountService bankAccountService;


    public BankAccountsController(final BankAccountService bankAccountService) {
      this.bankAccountService = bankAccountService;
    }

    @PostMapping("create")
    public ResponseEntity createBankAccount(@RequestBody final BankAccount bankAccount) {
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
}
