package za.co.ebank.bank.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import za.co.ebank.bank.validation.PasswordMatchValidator;

/**
 *
 * @author cliff
 */
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
@Documented
public @interface PasswordMatch {
    
    String message() default "Passwords don't match!";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String password();

    String confirmPassword();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        PasswordMatch[] value();
    }
}
