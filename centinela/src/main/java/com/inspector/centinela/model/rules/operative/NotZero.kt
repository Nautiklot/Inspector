package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.NotZero
import com.inspector.centinela.handler.ConstraintValidator

/**
 * Validator for the [NotZero] annotation.
 *
 * This validator ensures that a [Number] value is not zero.
 * It converts the value to a double for comparison, which covers
 * most common numeric types (Int, Long, Float, Double).
 */
class NotZeroValidator : ConstraintValidator<NotZero, Number> {

    /**
     * Validates that the numeric value is not zero.
     *
     * @param annotation The [NotZero] annotation instance.
     * @param value The numeric value to validate.
     * @return True if the value is not null and not zero, false otherwise.
     */
    override fun isValid(annotation: NotZero, value: Number?): Boolean =
        value != null && value.toDouble() != 0.0
}
