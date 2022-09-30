package za.co.ebank.bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBankAccount {
    private String accountNumber;
    private long  userId;
}
