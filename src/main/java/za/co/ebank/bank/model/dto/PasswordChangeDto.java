package za.co.ebank.bank.model.dto;

import lombok.Data;
import za.co.ebank.bank.annotation.ValidPassword;

/**
 *
 * @author cliff
 */
@Data
public class PasswordChangeDto {
    private long id;
    private String password;
    private String confirmPassword;
}
