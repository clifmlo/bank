package za.co.ebank.bank.service;

import java.math.BigDecimal;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import za.co.ebank.bank.model.enumeration.AccountType;
import za.co.ebank.bank.model.dto.CreateBankAccount;
import za.co.ebank.bank.model.dto.Deposit;
import za.co.ebank.bank.model.dto.TransactionDto;
import za.co.ebank.bank.model.persistence.BankAccount;
import za.co.ebank.bank.model.persistence.PaymentTransaction;
import za.co.ebank.bank.repo.BankAccountRepo;
import za.co.ebank.bank.exception.BankAccountException;

@SpringBootTest
@RunWith(SpringRunner.class)  
public class TransactionServiceTests {
    @Autowired
    BankAccountService bankAccountService;
    
    @Autowired
    TransactionService transactionService;
    
    @Autowired
    BankAccountRepo bankAccountRepo;

    @Test
    public void shouldPayAnotherAccount() throws BankAccountException {
        Deposit deposit = Deposit.builder()
                .accountNumber("1776579871")
                .amount(BigDecimal.valueOf(500))
                .reference("Initial Deposit")
                .build();
        
        CreateBankAccount createBankAccount = CreateBankAccount.builder()
                                    .accountType(AccountType.CHEQUE)
                                    .userId(1l)
                                    .deposit(deposit)
                                    .build();
        
        CreateBankAccount createBankAccount2 = CreateBankAccount.builder()
                                    .userId(1l)
                                    .deposit(deposit)
                                    .accountType(AccountType.SAVINGS)
                                    .build();
        
        BankAccount creditAccount = bankAccountService.createBankAccount(createBankAccount);
        BankAccount debitAccount = bankAccountService.createBankAccount(createBankAccount2);
        assertNotNull(creditAccount);  
        assertNotNull(debitAccount);  
        
        TransactionDto transactionDto = TransactionDto.builder()
                                        .creditAccount(creditAccount.getAccountNumber())
                                        .debitAccount(debitAccount.getAccountNumber())
                                        .transactionAmount(BigDecimal.valueOf(250.45))
                                        .reference("test transaction")
                                        .build();
        
        PaymentTransaction paymentTransaction = transactionService.payAnotherAccount(transactionDto);        
        BankAccount credAccount = bankAccountService.findByAccountNumber(creditAccount.getAccountNumber());
        BankAccount debAccount = bankAccountService.findByAccountNumber(debitAccount.getAccountNumber());
        
        assertNotNull(paymentTransaction);
        Assert.assertEquals(creditAccount.getAccountNumber(), paymentTransaction.getCreditAccount());
        Assert.assertEquals(debitAccount.getAccountNumber(), paymentTransaction.getDebitAccount());
        Assert.assertEquals("test transaction", paymentTransaction.getReference());
        Assert.assertEquals(BigDecimal.valueOf(250.45), paymentTransaction.getCreditEntry());
        
        Assert.assertEquals(BigDecimal.valueOf(249.55), debAccount.getAvailableBalance());
        Assert.assertEquals(BigDecimal.valueOf(750.45), credAccount.getAvailableBalance());
       
        
        bankAccountRepo.deleteById(creditAccount.getId());
        bankAccountRepo.deleteById(debitAccount.getId());
    }
}
