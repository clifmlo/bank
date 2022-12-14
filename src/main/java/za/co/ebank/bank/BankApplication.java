package za.co.ebank.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableScheduling
@SpringBootApplication
@EnableAsync
public class BankApplication {
    
    public static void main(String[] args) {
            SpringApplication.run(BankApplication.class, args);
    }
}
