package za.co.ebank.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.ebank.bank.model.UserAccount;

public interface UserAccountRepo extends JpaRepository<UserAccount, Long> {
}
