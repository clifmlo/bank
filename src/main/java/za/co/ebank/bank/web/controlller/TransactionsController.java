package za.co.ebank.bank.web.controlller;

import lombok.extern.slf4j.Slf4j;
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
import za.co.ebank.bank.exception.BankAccountException;
import za.co.ebank.bank.exception.UserAccountException;
import za.co.ebank.bank.model.ApiResponse;
import za.co.ebank.bank.model.dto.TransactionDto;

@RequestMapping("api/v1/transaction")
@RestController
@Slf4j
public class TransactionsController {
    
    final TransactionService transactionService;
    
    public TransactionsController (final TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    
    @PostMapping("deposit")
    public ResponseEntity deposit(@RequestBody final Deposit deposit) throws UserAccountException {
        return new ResponseEntity(transactionService.deposit(deposit), HttpStatus.OK);
    }
    
    @PostMapping("transfer")
    public ResponseEntity payAnotherAccount(@RequestBody final TransactionDto transaction) {
        try {
            return new ResponseEntity(transactionService.payAnotherAccount(transaction), HttpStatus.OK);
        } catch (BankAccountException ex){
            log.error(ex.getMessage());
            return new ResponseEntity(new ApiResponse(null, ex.getMessage(), true),  HttpStatus.BAD_REQUEST);     
        }
    }
    
    @GetMapping("{accountNumber}")
    public ResponseEntity findTransactionsByAccountNumber(@PathVariable final String accountNumber) {
        return new ResponseEntity(transactionService.findByAccountNumber(accountNumber), HttpStatus.OK);
    }
}
