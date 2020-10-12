package org.example.fileshibernate.validation;

import org.example.fileshibernate.dto.PasswordDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, PasswordDto> {


    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {

    }

    @Override
    public boolean isValid(PasswordDto dto, ConstraintValidatorContext context) {

        return dto.getPassword().equals(dto.getRepeatedPassword());

    }
}
