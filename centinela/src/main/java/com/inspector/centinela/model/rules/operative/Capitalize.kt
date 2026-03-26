package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.Capitalize
import com.inspector.centinela.handler.ConstraintValidator
import com.inspector.centinela.handler.DataTransformer

/**
 * Transformer for the [Capitalize] annotation.
 *
 * It ensures that the first character of the string is converted to title case.
 */
class CapitalizeTransformer : DataTransformer<Capitalize, String> {

    /**
     * Capitalizes the first character of the provided string.
     *
     * @param annotation The [Capitalize] annotation instance.
     * @param value The string to be transformed.
     * @return The transformed string with the first letter capitalized, or null if the input was null.
     */
    override fun transform(annotation: Capitalize, value: String?): String? =
        value?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

/**
 * Validator for the [Capitalize] annotation.
 *
 * It validates that a string starts with an uppercase letter.
 */
class CapitalizeValidator : ConstraintValidator<Capitalize, String> {

    /**
     * Checks if the first character of the string is uppercase.
     *
     * @param annotation The [Capitalize] annotation instance.
     * @param value The string to be validated.
     * @return True if the value is not null, not blank, and starts with an uppercase character, false otherwise.
     */
    override fun isValid(annotation: Capitalize, value: String?): Boolean =
        value != null && value.isNotBlank() && value.first().isUpperCase()
}