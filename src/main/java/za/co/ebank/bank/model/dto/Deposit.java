package za.co.ebank.bank.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import lombok.Builder;

@Data
@Builder
public class Deposit {
    private String accountNumber;
    private BigDecimal amount;
    private String reference;
}
