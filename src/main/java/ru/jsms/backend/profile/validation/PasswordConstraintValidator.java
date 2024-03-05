package ru.jsms.backend.profile.validation;

import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {

    private static final PasswordValidator validator = new PasswordValidator(Arrays.asList(
            new LengthRule(6, 30),
// Пока не будем требовать такое
//            new CharacterRule(EnglishCharacterData.UpperCase, 1),
//            new CharacterRule(EnglishCharacterData.LowerCase, 1),
//            new CharacterRule(EnglishCharacterData.Digit, 1),
//            new CharacterRule(EnglishCharacterData.Special, 1),
            new WhitespaceRule()
    ));

    @Override
    public void initialize(Password arg0) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        List<String> messages = validator.getMessages(result);

        context.buildConstraintViolationWithTemplate(String.join(" ", messages))
                .addConstraintViolation().disableDefaultConstraintViolation();
        return false;
    }
}
