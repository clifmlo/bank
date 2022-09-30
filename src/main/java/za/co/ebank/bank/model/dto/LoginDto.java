package za.co.ebank.bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String email;
    private String password; 
}
