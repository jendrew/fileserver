package org.example.fileshibernate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PasswordsMatchValidator.class})
public @interface PasswordsMatch {
    String message() default "Hasła nie są jednakowe";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};
}
