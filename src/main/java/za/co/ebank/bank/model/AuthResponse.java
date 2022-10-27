package za.co.ebank.bank.model;

import lombok.*;
import za.co.ebank.bank.model.persistence.UserAccount;

/**
 *
 * @author cliff
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthResponse {
    private UserAccount user;
    private String token;
}