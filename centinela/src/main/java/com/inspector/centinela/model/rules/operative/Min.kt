package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.Min
import com.inspector.centinela.handler.ConstraintValidator

/**
 * Validator for the [Min] annotation.
 *
 * It validates that a numeric value is greater than or equal to the
 * minimum value specified in the annotation.
 */
class MinValidator : ConstraintValidator<Min, Number> {

    /**
     * Checks if the provided numeric value satisfies the minimum constraint.
     *
     * @param annotation The [Min] annotation instance containing the minimum value.
     * @param value The number to be validated.
     * @return True if the value is not null and greater than or equal to [Min.min], false otherwise.
     */
    override fun isValid(annotation: Min, value: Number?): Boolean =
        value != null && value.toDouble() >= annotation.min
}
