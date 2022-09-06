package za.co.ebank.bank.model.dto;

import lombok.Data;

@Data
public class UserBankAccount {
    private String accountNumber;
    private long  userId;
}
