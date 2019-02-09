package helloworld.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserNameValidator implements ConstraintValidator<UserNameConstraint, String> {
    final String regex = "^[a-z]{1,8}\\.{1}[a-z]{1,8}$";

    @Override
    public void initialize(UserNameConstraint contactNumber) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            throw new RuntimeException();
        }
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }
}
