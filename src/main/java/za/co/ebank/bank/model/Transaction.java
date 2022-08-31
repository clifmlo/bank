package za.co.ebank.bank.model;

import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String credit_account;
    private String debit_account;
    private BigDecimal transaction_amount;
    private TransactionStatus transaction_status;
    private LocalDateTime date_received;
    private LocalDateTime date_processed;
    private LocalDateTime date_updated;
}
