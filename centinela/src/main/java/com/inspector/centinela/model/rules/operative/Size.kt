package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.Size
import com.inspector.centinela.handler.ConstraintValidator

/**
 * Validator for the [Size] annotation.
 *
 * It validates that the string representation of a value has a length within
 * the specified [Size.min] and [Size.max] range.
 */
class SizeValidator : ConstraintValidator<Size, Any> {

    /**
     * Checks if the length of the string representation of the value is within the allowed range.
     *
     * @param annotation The [Size] annotation instance containing min and max constraints.
     * @param value The value to be validated.
     * @return True if the value is not null and its string length is within range, false otherwise.
     */
    override fun isValid(annotation: Size, value: Any?): Boolean =
        value != null && value.toString().length in annotation.min..annotation.max
}
