package com.training.authentication.validator;

import com.training.authentication.constraint.RoleConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleConstraintValidator implements ConstraintValidator<RoleConstraint, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return (value.equals("ADMIN") || value.equals("USER"));
	}

}
