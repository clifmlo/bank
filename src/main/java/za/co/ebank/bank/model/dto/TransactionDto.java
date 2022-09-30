package za.co.ebank.bank.model.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *
 * @author cliff
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private String creditAccount;
    private String debitAccount;
    private BigDecimal transactionAmount;
    private String reference; 
}
