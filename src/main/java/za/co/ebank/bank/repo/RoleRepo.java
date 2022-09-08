package za.co.ebank.bank.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import za.co.ebank.bank.model.persistence.Role;

/**
 *
 * @author cliff
 */
public interface RoleRepo extends JpaRepository<Role, Long> {
    public Optional<Role> findByName(final String role);
}
