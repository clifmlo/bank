package za.co.ebank.bank.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import za.co.ebank.bank.model.persistence.Role;
import za.co.ebank.bank.model.persistence.UserAccount;
import za.co.ebank.bank.repo.UserAccountRepo;

/**
 *
 * @author cliff
 */
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    private final UserAccountRepo userRepository;

    public CustomUserDetailsService(UserAccountRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAccount user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email:" + email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}

