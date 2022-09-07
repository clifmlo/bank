package za.co.ebank.bank.exception;

/**
 *
 * @author cliff
 */
public class UserExistsException extends Throwable {
    
    public UserExistsException(final String message) {
        super(message);
    }    
}
