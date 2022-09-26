package za.co.ebank.bank.model.dto;

import lombok.Data;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import za.co.ebank.bank.model.AccountType;

@Data
@Builder
public class CreateBankAccount {
    private long userId;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private Deposit deposit;
}
