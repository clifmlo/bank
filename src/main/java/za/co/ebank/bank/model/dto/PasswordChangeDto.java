package za.co.ebank.bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.ebank.bank.annotation.ValidPassword;

/**
 *
 * @author cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDto {
    private long id;
    private String password;
    private String confirmPassword;
}
