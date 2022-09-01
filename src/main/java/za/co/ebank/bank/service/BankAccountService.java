package za.co.ebank.bank.service;

import za.co.ebank.bank.model.BankAccount;
import za.co.ebank.bank.repo.BankAccountRepo;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {
    private final BankAccountRepo bankAccountRepo;

    public BankAccountService(final BankAccountRepo bankAccountRepo) {
        this.bankAccountRepo = bankAccountRepo;
    }

    public BankAccount createBankAccount(final BankAccount bankAccount) {
        return bankAccountRepo.save(bankAccount);
    }
    
    public BankAccount findByAccountNumber(final String  accountNumber) {
        return bankAccountRepo.findByAccountNumber(accountNumber);
    }

}
