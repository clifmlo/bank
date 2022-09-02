package za.co.ebank.bank.service;

import za.co.ebank.bank.model.BankAccount;
import za.co.ebank.bank.repo.BankAccountRepo;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class BankAccountService {
    private final BankAccountRepo bankAccountRepo;

    public BankAccountService(final BankAccountRepo bankAccountRepo) {
        this.bankAccountRepo = bankAccountRepo;
    }

    public BankAccount createBankAccount(final BankAccount bankAccount) {
        return bankAccountRepo.save(bankAccount);
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
