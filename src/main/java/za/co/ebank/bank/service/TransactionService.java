package za.co.ebank.bank.service;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import za.co.ebank.bank.model.dto.Deposit;
import za.co.ebank.bank.model.persistence.PaymentTransaction;
import za.co.ebank.bank.repo.TransactionRepo;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import za.co.ebank.bank.model.persistence.BankAccount;
import za.co.ebank.bank.model.TransactionStatus;
import za.co.ebank.bank.model.BankAccountStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionService {
    private final TransactionRepo transactionRepo;
    private final BankAccountService bankAccountService;

    public TransactionService(final TransactionRepo transactionRepo, BankAccountService bankAccountService) {
        this.transactionRepo = transactionRepo;
        this.bankAccountService = bankAccountService;
    }

    public PaymentTransaction deposit(final Deposit deposit) {
        BankAccount account = bankAccountService.findByAccountNumber(deposit.getAccountNumber());
        if (account.getStatus().equals(BankAccountStatus.INACTIVE) && isNotNullOrZero(deposit.getAmount())) {
            account.setStatus(BankAccountStatus.ACTIVE);
        }
        account.setAvailableBalance(account.getAvailableBalance().add(deposit.getAmount()));
        account.setLatestBalance(account.getLatestBalance().add(deposit.getAmount()));
        bankAccountService.updateBankAccount(account);
        
        return createDepositTransactionEntry(deposit);
    }
    
    private boolean isNotNullOrZero(final BigDecimal value) {
        return value != null && !BigDecimal.ZERO.equals(value);
    }
    
    
    public PaymentTransaction payAnotherAccount(final PaymentTransaction transaction) {                
        BankAccount creditAccount = bankAccountService.findByAccountNumber(transaction.getCreditAccount());
        BankAccount debitAccount = bankAccountService.findByAccountNumber(transaction.getDebitAccount());
        
        debitAccount.setAvailableBalance(debitAccount.getAvailableBalance().subtract(transaction.getTransactionAmount()));       
        transaction.setDate_received(LocalDateTime.now());
        
        if (isSameClientAccounts(creditAccount, debitAccount)) {                     
            creditAccount.setAvailableBalance(creditAccount.getAvailableBalance().add(transaction.getTransactionAmount()));                       
            creditAccount.setLatestBalance(creditAccount.getLatestBalance().add(transaction.getTransactionAmount()));                       
            debitAccount.setLatestBalance(debitAccount.getLatestBalance().subtract(transaction.getTransactionAmount()));
            bankAccountService.updateBankAccount(debitAccount);
            bankAccountService.updateBankAccount(creditAccount);            
           
            transaction.setTransactionStatus(TransactionStatus.PROCESSED);
            transaction.setDate_processed(LocalDateTime.now());             
            transactionRepo.save(transaction);
        } else {            
            creditAccount.setLatestBalance(creditAccount.getLatestBalance().add(transaction.getTransactionAmount())); 
            transaction.setTransactionStatus(TransactionStatus.PENDING);                        
            transactionRepo.save(transaction);                      
        }
        
        
        
        return transaction;
    }

    private PaymentTransaction createDepositTransactionEntry(final Deposit deposit) {
        PaymentTransaction transaction = PaymentTransaction.builder()
                .creditAccount(deposit.getAccountNumber())
                .transactionAmount(deposit.getAmount())
                .date_received(LocalDateTime.now())
                .date_processed(LocalDateTime.now())
                .transactionStatus(TransactionStatus.PROCESSED)
                .reference(deposit.getReference())
                .build();
        
        return transactionRepo.save(transaction);
    }
    
    public PaymentTransaction updateTransaction(final PaymentTransaction transaction) {      
        return transactionRepo.save(transaction);
    }
    
    public List<PaymentTransaction> findByAccountNumber(final String accountNumber) {
        return transactionRepo.findByAccountNumber(accountNumber);
    }
    
    private boolean isSameClientAccounts(final BankAccount creditAccount, final BankAccount debitAccount){
        return creditAccount.getUser_account_id().longValue() == debitAccount.getUser_account_id().longValue();
    }
   
    public void processPendingTransactions() {
        List<PaymentTransaction> pendingTransactions = transactionRepo.findByStatus(TransactionStatus.PENDING);
        log.info("Pending transaction count: {}", pendingTransactions.size());         
        pendingTransactions.forEach(transaction ->processTransaction(transaction));
    }
    
    private void processTransaction(final PaymentTransaction transaction) {
        if (is10minAndOlder(transaction)) {
           log.info("processing transaction with Id: {}", transaction.getId());
           updateAccounts(transaction);
        }
    }
    
    private boolean is10minAndOlder(final PaymentTransaction transaction) {
        return Duration.between(transaction.getDate_received(), LocalDateTime.now()).toMinutes() > 10;
    }
    
    private void updateAccounts(final PaymentTransaction transaction) {
        BankAccount creditAccount = bankAccountService.findByAccountNumber(transaction.getCreditAccount());
        BankAccount debitAccount = bankAccountService.findByAccountNumber(transaction.getDebitAccount());         
        //Updated debit acount balances       
        debitAccount.setLatestBalance(debitAccount.getLatestBalance().subtract(transaction.getTransactionAmount()));
        
        //Updated credit acount balances
        creditAccount.setAvailableBalance(creditAccount.getAvailableBalance().add(transaction.getTransactionAmount()));
        
        //update the transaction
        transaction.setTransactionStatus(TransactionStatus.PROCESSED);
        transaction.setDate_processed(LocalDateTime.now()); 
        transaction.setDate_updated(LocalDateTime.now());
        
        bankAccountService.updateBankAccount(debitAccount);
        bankAccountService.updateBankAccount(creditAccount);
        transactionRepo.save(transaction);  
    }
}
