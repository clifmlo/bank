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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String credit_account;
    private String debit_account;
    private BigDecimal transaction_amount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transaction_status;
    @CreationTimestamp
    private LocalDateTime date_received;
    private LocalDateTime date_processed;
    @UpdateTimestamp
    private LocalDateTime date_updated;
}
