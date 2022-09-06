package za.co.ebank.bank.model;

import java.io.Serializable;
import lombok.Data;

import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.NoArgsConstructor;    
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String creditAccount;
    private String debitAccount;
    private BigDecimal transactionAmount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
    private String reference;
    @CreationTimestamp
    private LocalDateTime date_received;
    private LocalDateTime date_processed;
    @UpdateTimestamp
    private LocalDateTime date_updated;
}
