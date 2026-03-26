package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.NotBlank
import com.inspector.centinela.handler.ConstraintValidator

/**
 * Validator for the [NotBlank] annotation.
 *
 * It validates that a string value is not null and not blank.
 * A string is considered blank if it is empty or consists only of whitespace characters.
 */
class NotBlankValidator : ConstraintValidator<NotBlank, String> {

    /**
     * Checks if the provided string is not blank.
     *
     * @param annotation The [NotBlank] annotation instance.
     * @param value The string to be validated.
     * @return True if the value is not null and not blank, false otherwise.
     */
    override fun isValid(annotation: NotBlank, value: String?): Boolean =
        value != null && value.isNotBlank()
}