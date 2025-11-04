package com.warehouse.identity_api.validation.validator;

import com.warehouse.identity_api.validation.FieldNotEmpty;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldNotEmptyValidator implements ConstraintValidator<FieldNotEmpty, String> {
  private String field;

  @Override
  public void initialize(FieldNotEmpty constraintAnnotation) {
    this.field = constraintAnnotation.field();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.trim().isEmpty()) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(field + " cannot be empty")
              .addConstraintViolation();
      return false;
    }
    return true;
  }
}
