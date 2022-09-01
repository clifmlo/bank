package za.co.ebank.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import za.co.ebank.bank.model.BankAccount;

public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {
    
    @Query("SELECT b FROM BankAccount b WHERE b.accountNumber = ?1")
    public BankAccount findByAccountNumber (final String accountNumber);
}
