package za.co.ebank.bank.model.persistence;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
    
    @Column(unique=true)
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
    
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "user_role",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;
    
    @CreationTimestamp
    private LocalDateTime date_created;
    
    @UpdateTimestamp
    private LocalDateTime date_updated;
}
