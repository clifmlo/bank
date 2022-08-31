package za.co.ebank.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.ebank.bank.model.BankAccount;

public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {
}
