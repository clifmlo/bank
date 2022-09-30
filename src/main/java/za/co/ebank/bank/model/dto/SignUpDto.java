package za.co.ebank.bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author cliff
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
    private String name;
    private String surname;
    private String idNumber;   
    private String email;
    private String contactNumber;
    private String password;
    private String confirmPassword;
}
