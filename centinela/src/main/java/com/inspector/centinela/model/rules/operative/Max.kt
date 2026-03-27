package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.Max
import com.inspector.centinela.handler.ConstraintValidator

/**
 * Validator for the [Max] annotation.
 *
 * It validates that a numeric value is less than or equal to the
 * maximum value specified in the annotation.
 */
class MaxValidator : ConstraintValidator<Max, Number> {

    /**
     * Checks if the provided numeric value satisfies the maximum constraint.
     *
     * @param annotation The [Max] annotation instance containing the maximum value.
     * @param value The number to be validated.
     * @return True if the value is not null and less than or equal to [Max.max], false otherwise.
     */
    override fun isValid(annotation: Max, value: Number?): Boolean =
        value != null && value.toDouble() <= annotation.max
}