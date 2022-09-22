package za.co.ebank.bank.model.persistence;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import za.co.ebank.bank.model.AccountType;
import za.co.ebank.bank.model.BankAccountStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="bank_account")
@Data
public class BankAccount implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;  
    
    private String accountNumber;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    
    @Enumerated(EnumType.STRING)
    private BankAccountStatus status;
    
    private BigDecimal availableBalance;
    
    private BigDecimal latestBalance;
    
    private Long user_account_id;
    
    @CreationTimestamp
    private LocalDateTime date_created;
    
    @UpdateTimestamp
    private LocalDateTime date_updated;
}
