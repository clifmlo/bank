package za.co.ebank.bank.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import za.co.ebank.bank.service.TransactionService;

/**
 *
 * @author cliff
 */
@Slf4j
@Component
public class TransactionsScheduler {
    final TransactionService transactionService;
    
    public TransactionsScheduler(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    
    //@Scheduled(cron = "0 * * * * *") //cron every minute
    public void processPengingTransactions() {
        log.info("cron started...");
        transactionService.processPendingTransactions();//poll for pending transactions every minute, update acount balances for those that are 10min and older
        log.info("cron finished.");
    }
}    
    
