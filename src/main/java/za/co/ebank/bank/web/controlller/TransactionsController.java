package za.co.ebank.bank.web.controlller;

import org.springframework.web.bind.annotation.RequestMapping;
import za.co.ebank.bank.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import za.co.ebank.bank.model.dto.Deposit;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import za.co.ebank.bank.model.dto.TransactionDto;

@RequestMapping("api/v1/transaction")
@RestController
public class TransactionsController {
    
    final TransactionService transactionService;
    
    public TransactionsController (final TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    
    @PostMapping("deposit")
    public ResponseEntity deposit(@RequestBody final Deposit deposit) {
        return new ResponseEntity(transactionService.deposit(deposit), HttpStatus.OK);
    }
    
    @PostMapping("transfer")
    public ResponseEntity payAnotherAccount(@RequestBody final TransactionDto transaction) {
        return new ResponseEntity(transactionService.payAnotherAccount(transaction), HttpStatus.OK);
    }
    
    @GetMapping("{accountNumber}")
    public ResponseEntity findByAccountNumber(@PathVariable final String accountNumber) {
        return new ResponseEntity(transactionService.findByAccountNumber(accountNumber), HttpStatus.OK);
    }
}
