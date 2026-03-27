package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.Positive
import com.inspector.centinela.handler.ConstraintValidator

/**
 * Validator for the [Positive] annotation.
 *
 * It validates that a numeric value is strictly greater than zero.
 */
class PositiveValidator : ConstraintValidator<Positive, Number> {

    /**
     * Checks if the provided numeric value is positive.
     *
     * @param annotation The [Positive] annotation instance.
     * @param value The number to be validated.
     * @return True if the value is not null and greater than 0.0, false otherwise.
     */
    override fun isValid(annotation: Positive, value: Number?): Boolean =
        value != null && value.toDouble() > 0.0
}