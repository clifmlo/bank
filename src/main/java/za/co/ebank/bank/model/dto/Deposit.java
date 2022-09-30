package za.co.ebank.bank.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Deposit {
    private String accountNumber;
    private BigDecimal amount;
    private String reference;
}
