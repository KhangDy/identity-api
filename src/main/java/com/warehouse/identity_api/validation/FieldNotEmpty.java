package com.warehouse.identity_api.validation;

import com.warehouse.identity_api.validation.validator.FieldNotEmptyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FieldNotEmptyValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldNotEmpty {
  String message() default "Field cannot be empty";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  String field() default "";
}
