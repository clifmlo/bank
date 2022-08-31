package za.co.ebank.bank.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Table(name="\"User\"") //escape user as it is a reserved term
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String idNumber;
    private String email;
    private String contactNumber;
    private LocalDateTime date_created;
    private LocalDateTime date_updated;
}
