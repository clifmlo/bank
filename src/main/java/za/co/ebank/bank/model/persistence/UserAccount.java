package za.co.ebank.bank.model.persistence;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import za.co.ebank.bank.annotation.PasswordMatch;
import za.co.ebank.bank.annotation.ValidPassword;


@PasswordMatch.List({
    @PasswordMatch(
            password = "password",
            confirmPassword = "confirmPassword",
            message = "Passwords do not match!"
    )
})
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @NotBlank(message = "Name is mandatory")
    private String name;
    
    @NotNull
    @NotBlank(message = "Surname is mandatory")
    private String surname;
    
    @NotNull
    @NotBlank(message = "Id number is mandatory")
    private String idNumber;
    
    @NotNull
    @Email(message = "Email is not valid.", regexp ="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
    private String email;
    
    @NotNull
    @NotBlank(message = "Contact number is mandatory")
    private String contactNumber;
    
    @ValidPassword
    @NotNull
    @NotBlank(message = "Password is mandatory.")
    private String password;
   
    @ValidPassword
    @NotNull
    @NotBlank(message = "Confirm password is mandatory.")
    @Transient
    private String confirmPassword;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_account_id")
    private List<BankAccount> bankAccounts;
    
    @CreationTimestamp
    private LocalDateTime date_created;
    
    @UpdateTimestamp
    private LocalDateTime date_updated;
}
