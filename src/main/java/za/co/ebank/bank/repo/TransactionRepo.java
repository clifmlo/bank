package za.co.ebank.bank.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import za.co.ebank.bank.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import za.co.ebank.bank.model.TransactionStatus;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.creditAccount = ?1 OR t.debitAccount = ?1")
    public List <Transaction> findByAccountNumber (final String accountNumber);

    @Query("SELECT t FROM Transaction t WHERE t.transactionStatus =?1")
    public List<Transaction> findByStatus(final TransactionStatus status);
}
