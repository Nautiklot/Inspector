package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.Regex
import com.inspector.centinela.handler.ConstraintValidator

/**
 * Validator for the [Regex] annotation.
 *
 * It validates that a string value matches the regular expression pattern
 * specified in the annotation.
 */
class RegexValidator : ConstraintValidator<Regex, String> {

    /**
     * Checks if the provided string matches the regular expression pattern.
     *
     * @param annotation The [Regex] annotation instance containing the pattern.
     * @param value The string to be validated.
     * @return True if the value is not null and matches the pattern, false otherwise.
     */
    override fun isValid(annotation: Regex, value: String?): Boolean =
        value != null && kotlin.text.Regex(annotation.regex).matches(value)
}
