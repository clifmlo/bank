package za.co.ebank.bank.model.dto;

import lombok.Data;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import za.co.ebank.bank.model.AccountType;

@Data
public class CreateBankAccount {
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private Deposit deposit;
}
