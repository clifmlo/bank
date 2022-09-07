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
    @NotBlank
    private String name;
    
    @NotNull
    @NotBlank
    private String surname;
    
    @NotNull
    @NotBlank
    private String idNumber;
    
    @NotNull
    @Email(message = "Email is not valid.", regexp ="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
    private String email;
    
    @NotNull
    @NotBlank
    private String contactNumber;
    
    @ValidPassword
    @NotNull
    @NotBlank
    private String password;
   
    @ValidPassword
    @NotNull
    @NotBlank
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
