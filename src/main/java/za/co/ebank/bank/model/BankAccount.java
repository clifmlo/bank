package za.co.ebank.bank.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name="bank_account")
@Data
public class BankAccount implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private String accountNumber;
    private BigDecimal availableBalance;
    private BigDecimal latestBalance;
    private Long user_account_id;
    @CreationTimestamp
    private LocalDateTime date_created;
    @UpdateTimestamp
    private LocalDateTime date_updated;
}
