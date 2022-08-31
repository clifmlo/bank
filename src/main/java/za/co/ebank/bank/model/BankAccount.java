package za.co.ebank.bank.model;

import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="bank_account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private AccountType accountType;
    private String accountNumber;
    private BigDecimal availableBalance;
    private BigDecimal latestBalance;
    private LocalDateTime date_created;
    private LocalDateTime date_updated;
}
