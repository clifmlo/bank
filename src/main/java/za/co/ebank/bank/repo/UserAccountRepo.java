package za.co.ebank.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import za.co.ebank.bank.model.persistence.UserAccount;

public interface UserAccountRepo extends JpaRepository<UserAccount, Long> {
    @Query("SELECT u FROM UserAccount u WHERE u.email = ?1")
    public UserAccount findByEmail(final String email);
}
