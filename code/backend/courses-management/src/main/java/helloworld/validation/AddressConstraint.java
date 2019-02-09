package helloworld.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AddressValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AddressConstraint {
    String message() default "XML parsing error";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
