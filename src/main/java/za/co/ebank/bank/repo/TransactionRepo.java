package za.co.ebank.bank.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import za.co.ebank.bank.model.persistence.PaymentTransaction;
import org.springframework.data.jpa.repository.Query;
import za.co.ebank.bank.model.enumeration.TransactionStatus;

public interface TransactionRepo extends JpaRepository<PaymentTransaction, Long> {
    @Query("SELECT t FROM PaymentTransaction t WHERE t.creditAccount = ?1 OR t.debitAccount = ?1")
    public List <PaymentTransaction> findByAccountNumber (final String accountNumber);

    @Query("SELECT t FROM PaymentTransaction t WHERE t.transactionStatus =?1")
    public List<PaymentTransaction> findByStatus(final TransactionStatus status);
}
