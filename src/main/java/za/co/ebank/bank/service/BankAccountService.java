package za.co.ebank.bank.service;

import za.co.ebank.bank.model.persistence.BankAccount;
import za.co.ebank.bank.repo.BankAccountRepo;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import javax.transaction.Transactional;
import za.co.ebank.bank.exception.BankAccountException;
import za.co.ebank.bank.model.enumeration.BankAccountStatus;
import za.co.ebank.bank.model.dto.CreateBankAccount;

@Service
@Transactional
public class BankAccountService {
    private final BankAccountRepo bankAccountRepo;

    public BankAccountService(final BankAccountRepo bankAccountRepo) {
        this.bankAccountRepo = bankAccountRepo;
    }

    public BankAccount createBankAccount(final CreateBankAccount bankAccount) {
      
        if (bankAccount.getDeposit().getAmount() == null) {
            bankAccount.getDeposit().setAmount(BigDecimal.ZERO);
        }
     
        BankAccount account = BankAccount.builder()
                               .accountNumber(generateAccountNumber())
                               .user_account_id(bankAccount.getUserId())
                               .accountType(bankAccount.getAccountType())
                               .availableBalance(bankAccount.getDeposit().getAmount())
                               .latestBalance(bankAccount.getDeposit().getAmount())
                               .date_created(LocalDateTime.now())
                               .build();
        
        if (isNotNullOrZero(bankAccount.getDeposit().getAmount())) {
            account.setStatus(BankAccountStatus.ACTIVE); 
        } else {
            account.setStatus(BankAccountStatus.INACTIVE); 
        }
           
        return bankAccountRepo.save(account);
    }
    
    private String generateAccountNumber() {        
        Random rand = new Random();       
        return String.valueOf(1000000000 + (int) (rand.nextDouble() * 999999999)); 
    }
    
    private boolean isNotNullOrZero(final BigDecimal value) {
        return value != null && !BigDecimal.ZERO.equals(value);
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

    public List<BankAccount> findBankAccountsByUserId(final long userId) {
        return bankAccountRepo.findByUserAccountId(userId);
    }
    
    public void deleteAccount(final long id) throws BankAccountException{
        BankAccount account = bankAccountRepo.findById(id).get();
        if (account.getAvailableBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new BankAccountException("You cannot delete an account with positive balance.");
        }
        if (account.getAvailableBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new BankAccountException("You cannot delete an account with negative balance.");
        }
        bankAccountRepo.deleteById(id);
    }
}
