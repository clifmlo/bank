package za.co.ebank.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.ebank.bank.model.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
}
