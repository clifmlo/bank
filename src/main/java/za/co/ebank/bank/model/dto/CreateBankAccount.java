package za.co.ebank.bank.model.dto;

import lombok.Data;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import za.co.ebank.bank.model.enumeration.AccountType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBankAccount {
    private long userId;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private Deposit deposit;
}
