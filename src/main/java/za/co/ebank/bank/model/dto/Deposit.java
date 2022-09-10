package za.co.ebank.bank.model.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Deposit {
    private String accountNumber;
    private BigDecimal amount;
    private String reference;
}
