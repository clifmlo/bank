package za.co.ebank.bank.model.persistence;

import java.io.Serializable;
import lombok.Data;

import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import lombok.Builder;
import lombok.NoArgsConstructor;    
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import za.co.ebank.bank.model.enumeration.TransactionStatus;

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
    
    private BigDecimal debitEntry;
    
    private BigDecimal creditEntry;
    
    private BigDecimal debitBalance;
    
    private BigDecimal creditBalance;
    
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
    
    private String reference;
    
    @Transient
    private String transactionType;
    
    @CreationTimestamp
    private LocalDateTime date_received;
    
    private LocalDate date_processed;
    
    @UpdateTimestamp
    private LocalDateTime date_updated;
}
