package za.co.ebank.bank.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
public class UserAccount implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String idNumber;
    private String email;
    private String contactNumber;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_account_id")
    private List<BankAccount> bankAccounts;
    @CreationTimestamp
    private LocalDateTime date_created;
    @UpdateTimestamp
    private LocalDateTime date_updated;
}
