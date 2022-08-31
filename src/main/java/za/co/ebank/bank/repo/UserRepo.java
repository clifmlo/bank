package za.co.ebank.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.ebank.bank.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
}
