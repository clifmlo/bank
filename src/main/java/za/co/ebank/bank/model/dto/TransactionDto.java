package za.co.ebank.bank.model.dto;

import java.math.BigDecimal;
import lombok.Data;
/**
 *
 * @author cliff
 */
@Data
public class TransactionDto {
    private String creditAccount;
    private String debitAccount;
    private BigDecimal transactionAmount;
    private String reference; 
}
