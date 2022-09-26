package za.co.ebank.bank.service;

import java.math.BigDecimal;
import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import za.co.ebank.bank.model.AccountType;
import za.co.ebank.bank.model.dto.CreateBankAccount;
import za.co.ebank.bank.model.dto.Deposit;
import za.co.ebank.bank.model.persistence.BankAccount;

@SpringBootTest
@RunWith(SpringRunner.class)         
public class BankAccountServiceTests {
    
    @Autowired
    BankAccountService bankAccountService;

    @Test
    void shouldCreateBankAccount() {
        Deposit deposit = Deposit.builder()
                .accountNumber("9876579876")
                .amount(BigDecimal.valueOf(25000))
                .reference("Initial Deposit")
                .build();
        
        CreateBankAccount createBankAccount = CreateBankAccount.builder()
                                .userId(1l)
                                .accountType(AccountType.CREDIT)
                                .deposit(deposit)
                                .build();
        
        BankAccount account  = bankAccountService.createBankAccount(createBankAccount);
        
        assertNotNull(account);
    }
}
