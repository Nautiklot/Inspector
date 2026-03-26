package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.Negative
import com.inspector.centinela.handler.ConstraintValidator

/**
 * Validator for the [Negative] annotation.
 *
 * It validates that a numeric value is strictly less than zero.
 */
class NegativeValidator : ConstraintValidator<Negative, Number> {

    /**
     * Checks if the provided numeric value is negative.
     *
     * @param annotation The [Negative] annotation instance.
     * @param value The number to be validated.
     * @return True if the value is not null and less than 0, false otherwise.
     */
    override fun isValid(annotation: Negative, value: Number?): Boolean =
        value != null && value.toDouble() < 0.0
}