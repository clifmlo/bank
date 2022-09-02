package za.co.ebank.bank.service;

import org.springframework.stereotype.Service;
import za.co.ebank.bank.model.Deposit;
import za.co.ebank.bank.model.Transaction;
import za.co.ebank.bank.repo.TransactionRepo;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import za.co.ebank.bank.model.BankAccount;
import za.co.ebank.bank.model.TransactionStatus;

@Service
public class TransactionService {
    private final TransactionRepo transactionRepo;
    private final BankAccountService bankAccountService;

    public TransactionService(final TransactionRepo transactionRepo, BankAccountService bankAccountService) {
        this.transactionRepo = transactionRepo;
        this.bankAccountService = bankAccountService;
    }

    public Transaction deposit(final Deposit deposit) {
        BigDecimal currentBalance = bankAccountService.getAvailableBalance(deposit.getCreditAccount());
        BigDecimal newBalance = currentBalance.add( deposit.getDepositAmount());
        BankAccount account = bankAccountService.findByAccountNumber(deposit.getCreditAccount());
        account.setAvailableBalance(newBalance);
        bankAccountService.updateBankAccount(account);
        
        return createDepositTransactionEntry(deposit);
    }
    
    public Transaction payAnotherAccount(final Transaction transaction) {                
        BankAccount creditAccount = bankAccountService.findByAccountNumber(transaction.getCreditAccount());
        BankAccount debitAccount = bankAccountService.findByAccountNumber(transaction.getDebitAccount());
        
        debitAccount.setAvailableBalance(debitAccount.getAvailableBalance().subtract(transaction.getTransactionAmount()));       
        transaction.setDate_received(LocalDateTime.now());
        
        if (isSameClientAccounts(creditAccount, debitAccount)) {                     
            creditAccount.setAvailableBalance(creditAccount.getAvailableBalance().add(transaction.getTransactionAmount()));                       
            bankAccountService.updateBankAccount(debitAccount);
            bankAccountService.updateBankAccount(creditAccount);            
           
            transaction.setTransactionStatus(TransactionStatus.PROCESSED);
            transaction.setDate_processed(LocalDateTime.now());             
            transactionRepo.save(transaction);
        } else {
            //DEBIT
            //available has been reduced above
            // dont touch latest 
            //therefore dont do nothing to debit
            
            
            //CREDIT
            //dont touch available yet
            //increase latest balance(Pending incoming)
            //set transaction to PENDING
            creditAccount.setLatestBalance(creditAccount.getLatestBalance().add(transaction.getTransactionAmount())); 
            transaction.setTransactionStatus(TransactionStatus.PENDING);                        
            transactionRepo.save(transaction);                      
        }
        
        
        
        return transaction;
    }

    
    private Transaction createDepositTransactionEntry(final Deposit deposit) {
        Transaction transaction = Transaction.builder()
                .creditAccount(deposit.getCreditAccount())
                .transactionAmount(deposit.getDepositAmount())
                .date_received(LocalDateTime.now())
                .date_processed(LocalDateTime.now())
                .transactionStatus(TransactionStatus.PROCESSED)
                .build();
        
        return transactionRepo.save(transaction);
    }
    
    public Transaction updateTransaction(final Transaction transaction) {      
        return transactionRepo.save(transaction);
    }
    
    public List<Transaction> findByAccountNumber(final String accountNumber) {
        return transactionRepo.findByAccountNumber(accountNumber);
    }
    
    private boolean isSameClientAccounts(final BankAccount creditAccount, final BankAccount debitAccount){
        return creditAccount.getUser_account_id().longValue() == debitAccount.getUser_account_id().longValue();
    }
    
}
