package za.co.ebank.bank.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Deposit {
    private String creditAccount;
    private BigDecimal depositAmount;
}
