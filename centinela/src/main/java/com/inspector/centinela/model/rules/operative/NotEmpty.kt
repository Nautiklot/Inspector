package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.NotEmpty
import com.inspector.centinela.handler.ConstraintValidator

/**
 * Validator for the [NotEmpty] annotation.
 *
 * It validates that a string value is not null and not empty.
 */
class NotEmptyValidator : ConstraintValidator<NotEmpty, String> {

    /**
     * Checks if the provided string is not empty.
     *
     * @param annotation The [NotEmpty] annotation instance.
     * @param value The string to be validated.
     * @return True if the value is not null and its length is greater than 0, false otherwise.
     */
    override fun isValid(annotation: NotEmpty, value: String?): Boolean =
        value != null && value.isNotEmpty()
}
