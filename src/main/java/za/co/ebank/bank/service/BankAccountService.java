package za.co.ebank.bank.service;

import za.co.ebank.bank.model.persistence.BankAccount;
import za.co.ebank.bank.repo.BankAccountRepo;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import za.co.ebank.bank.model.BankAccountStatus;
import za.co.ebank.bank.model.dto.CreateBankAccount;

@Service
public class BankAccountService {
    private final BankAccountRepo bankAccountRepo;

    public BankAccountService(final BankAccountRepo bankAccountRepo) {
        this.bankAccountRepo = bankAccountRepo;
    }

    public BankAccount createBankAccount(final CreateBankAccount bankAccount) {
      
        if (bankAccount.getDeposit().getDepositAmount() == null) {
            bankAccount.getDeposit().setDepositAmount(BigDecimal.ZERO);
        }
     
        BankAccount account = BankAccount.builder()
                               .accountNumber(generateAccountNumber())
                               .accountType(bankAccount.getAccountType())
                               .availableBalance(bankAccount.getDeposit().getDepositAmount())
                               .latestBalance(bankAccount.getDeposit().getDepositAmount())
                               .date_created(LocalDateTime.now())
                               .build();
        
        if (isNotNullOrZero(bankAccount.getDeposit().getDepositAmount())) {
            account.setStatus(BankAccountStatus.ACTIVE); 
        } else {
            account.setStatus(BankAccountStatus.INACTIVE); 
        }
           
        return bankAccountRepo.save(account);
    }
    
    private boolean isNotNullOrZero(final BigDecimal value) {
        return value != null && !BigDecimal.ZERO.equals(value);
    }
    
    private String generateAccountNumber() {
        
    //TODO generate proper account numbers
        return "123456789";
    }
    
    public BankAccount updateBankAccount(final BankAccount bankAccount) {
        return bankAccountRepo.save(bankAccount);
    }
    
    public BankAccount findByAccountNumber(final String  accountNumber) {
        return bankAccountRepo.findByAccountNumber(accountNumber);
    }
    
    public BigDecimal getLatesBalance (final String accountNumber) {
        return bankAccountRepo.findByAccountNumber(accountNumber).getLatestBalance();
    }
    
    public BigDecimal getAvailableBalance (final String accountNumber) {
        return bankAccountRepo.findByAccountNumber(accountNumber).getAvailableBalance();
    }
    
   

}
