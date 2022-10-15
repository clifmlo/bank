package za.co.ebank.bank.service;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import za.co.ebank.bank.model.dto.Deposit;
import za.co.ebank.bank.model.persistence.PaymentTransaction;
import za.co.ebank.bank.repo.TransactionRepo;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import za.co.ebank.bank.model.persistence.BankAccount;
import za.co.ebank.bank.model.enumeration.TransactionStatus;
import za.co.ebank.bank.model.enumeration.BankAccountStatus;
import lombok.extern.slf4j.Slf4j;
import za.co.ebank.bank.exception.BankAccountException;
import za.co.ebank.bank.exception.UserAccountException;
import za.co.ebank.bank.model.dto.TransactionDto;

@Slf4j
@Service
@Transactional
public class TransactionService {
    private final TransactionRepo transactionRepo;
    private final BankAccountService bankAccountService;
    private final UserAccountService userAccountService;

    public TransactionService(final TransactionRepo transactionRepo, BankAccountService bankAccountService, UserAccountService userAccountService) {
        this.transactionRepo = transactionRepo;
        this.bankAccountService = bankAccountService;
        this.userAccountService = userAccountService;
    }

    public PaymentTransaction deposit(final Deposit deposit) throws UserAccountException {
        BankAccount account = bankAccountService.findByAccountNumber(deposit.getAccountNumber());
        validateUserAccount(account);
        if (account.getStatus().equals(BankAccountStatus.INACTIVE) && isNotNullOrZero(deposit.getAmount())) {
            account.setStatus(BankAccountStatus.ACTIVE);
        }
        account.setAvailableBalance(account.getAvailableBalance().add(deposit.getAmount()));
        account.setLatestBalance(account.getLatestBalance().add(deposit.getAmount()));
        bankAccountService.updateBankAccount(account);
        
        return createDepositTransactionEntry(deposit, account);
    }
    
    private boolean isNotNullOrZero(final BigDecimal value) {
        return value != null && !BigDecimal.ZERO.equals(value);
    }
    
    private void validateUserAccount(final BankAccount account) throws UserAccountException {
        if(!userAccountService.findById(account.getUser_account_id()).get().isActive())
        throw new UserAccountException("Bank account is deactivated: " + account.getAccountNumber());    
    }
    
    public PaymentTransaction payAnotherAccount(final TransactionDto transactionDto) throws BankAccountException{          
        validateAccounts(transactionDto);
        BankAccount creditAccount = bankAccountService.findByAccountNumber(transactionDto.getCreditAccount());
        BankAccount debitAccount = bankAccountService.findByAccountNumber(transactionDto.getDebitAccount());
        validateDebitAccount(debitAccount, transactionDto); 
        validateCreditAccount(creditAccount);
        PaymentTransaction transaction = mapTransaction(transactionDto);                
        debitAccount.setAvailableBalance(debitAccount.getAvailableBalance().subtract(transaction.getCreditEntry()));       
        transaction.setDate_received(LocalDateTime.now());
        transaction.setDebitEntry(transaction.getCreditEntry().negate());
        transaction.setDebitBalance(debitAccount.getAvailableBalance());
        
        if (isSameClientAccounts(creditAccount, debitAccount)) {                     
            creditAccount.setAvailableBalance(creditAccount.getAvailableBalance().add(transaction.getCreditEntry()));                       
            creditAccount.setLatestBalance(creditAccount.getLatestBalance().add(transaction.getCreditEntry()));                       
            debitAccount.setLatestBalance(debitAccount.getLatestBalance().subtract(transaction.getCreditEntry()));
            bankAccountService.updateBankAccount(debitAccount);
            bankAccountService.updateBankAccount(creditAccount);            
           
            
            transaction.setCreditEntry(transaction.getCreditEntry());
            
            transaction.setCreditBalance(creditAccount.getAvailableBalance());
            transaction.setTransactionStatus(TransactionStatus.PROCESSED);
            transaction.setDate_processed(LocalDateTime.now());             
            transactionRepo.save(transaction);
        } else {                       
            transaction.setTransactionStatus(TransactionStatus.PENDING);                        
            transactionRepo.save(transaction);                      
        }
        
        return transaction;
    }
    
    private boolean accountInActive(BankAccount account){
        return account.getStatus().equals(BankAccountStatus.INACTIVE);
    }
    
    private void validateDebitAccount(final BankAccount debitAccount, final TransactionDto transactionDto) throws BankAccountException {        
        if (accountInActive(debitAccount)) {
            throw new BankAccountException("Account is inactive: "+ debitAccount.getAccountNumber());
        } 
        if (debitAccount.getAvailableBalance().compareTo(transactionDto.getTransactionAmount()) < 0 ) {
            throw new BankAccountException("Insufficient funds");
        } 
    }
    private void validateCreditAccount(final BankAccount creditAccount) throws BankAccountException {        
        if (accountInActive(creditAccount)) {
            throw new BankAccountException("Account is inactive: "+ creditAccount.getAccountNumber());
        } 
    }
    
    private void validateAccounts(final TransactionDto transactionDto) throws BankAccountException {
        if (transactionDto.getCreditAccount().equals(transactionDto.getDebitAccount())) {
            throw new BankAccountException("Debit account is same as credit account");
        } 
    }
    
    private PaymentTransaction mapTransaction(TransactionDto transactionDto) {
        return PaymentTransaction.builder()
                .creditAccount(transactionDto.getCreditAccount())
                .debitAccount(transactionDto.getDebitAccount())
                .creditEntry(transactionDto.getTransactionAmount())
                .reference(transactionDto.getReference())
                .build();
    }
    
    private PaymentTransaction createDepositTransactionEntry(final Deposit deposit, final BankAccount account) {
        PaymentTransaction transaction = PaymentTransaction.builder()
                .creditAccount(deposit.getAccountNumber())
                .creditEntry(deposit.getAmount())
                .creditBalance(account.getAvailableBalance())
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
        List<PaymentTransaction> transactions = transactionRepo.findByAccountNumber(accountNumber);        
        transactions.forEach(trans -> classifyTransaction(trans, accountNumber));
        
        return transactions;
    }
    
    private void classifyTransaction(PaymentTransaction transaction, final String accountNumber) {
        if(transaction.getCreditAccount().equals(accountNumber)) {
            transaction.setTransactionType("CREDIT");
        }else if(transaction.getDebitAccount().equals(accountNumber)){
            transaction.setTransactionType("DEBIT");
        }
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
        debitAccount.setLatestBalance(debitAccount.getLatestBalance().subtract(transaction.getCreditEntry()));
        
        //Updated credit acount balances
        creditAccount.setAvailableBalance(creditAccount.getAvailableBalance().add(transaction.getCreditEntry()));
        creditAccount.setLatestBalance(creditAccount.getLatestBalance().add(transaction.getCreditEntry())); 
        
        //update the transaction
        transaction.setDebitEntry(transaction.getCreditEntry().negate());
        transaction.setCreditEntry(transaction.getCreditEntry());
        transaction.setDebitBalance(debitAccount.getAvailableBalance());
        transaction.setCreditBalance(creditAccount.getAvailableBalance());
        transaction.setTransactionStatus(TransactionStatus.PROCESSED);
        transaction.setDate_processed(LocalDateTime.now()); 
        transaction.setDate_updated(LocalDateTime.now());
        
        bankAccountService.updateBankAccount(debitAccount);
        bankAccountService.updateBankAccount(creditAccount);
        transactionRepo.save(transaction);  
    }
}
