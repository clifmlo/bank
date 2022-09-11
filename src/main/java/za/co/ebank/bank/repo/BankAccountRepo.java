package za.co.ebank.bank.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import za.co.ebank.bank.model.persistence.BankAccount;

public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {
    
    @Query("SELECT b FROM BankAccount b WHERE b.accountNumber = ?1")
    public BankAccount findByAccountNumber (final String accountNumber);
    
    @Query("SELECT b FROM BankAccount b WHERE b.user_account_id = ?1")
    public List<BankAccount> findByUserAccountId(final long userId);
}
