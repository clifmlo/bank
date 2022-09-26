package za.co.ebank.bank.model.dto;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author cliff
 */
@Data
@Builder
public class SignUpDto {
    private String name;
    private String surname;
    private String idNumber;   
    private String email;
    private String contactNumber;
    private String password;
    private String confirmPassword;
}
