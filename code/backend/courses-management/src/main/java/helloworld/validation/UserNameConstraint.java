package helloworld.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserNameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserNameConstraint {
    String message() default "Invalid username. Format : firstname.lastname with 8 characters max before/after the dot.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
