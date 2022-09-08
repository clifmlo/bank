package za.co.ebank.bank.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import za.co.ebank.bank.model.persistence.UserAccount;

public interface UserAccountRepo extends JpaRepository<UserAccount, Long> {
    public Optional<UserAccount> findByEmail(final String email);
}
