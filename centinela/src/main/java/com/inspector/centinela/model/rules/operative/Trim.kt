package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.Trim
import com.inspector.centinela.handler.DataTransformer

/**
 * Transformer for the [Trim] annotation.
 *
 * It removes leading and trailing characters from a string. The specific characters
 * to be removed are defined in the [Trim.chars] property of the annotation.
 */
class TrimTransformer : DataTransformer<Trim, String> {

    /**
     * Trims the provided string based on the characters specified in the [Trim] annotation.
     *
     * If the specified characters are the default (a single space), it performs a standard
     * whitespace trim. Otherwise, it trims all occurrences of the characters provided
     * in the [Trim.chars] array from both ends of the string.
     *
     * @param annotation The [Trim] annotation instance containing the characters to remove.
     * @param value The string to be trimmed.
     * @return The trimmed string, or null if the input value was null.
     */
    override fun transform(annotation: Trim, value: String?): String? =
        value?.let {
            if (annotation.chars.contentEquals(charArrayOf(' '))) {
                it.trim()
            } else {
                it.trim(*annotation.chars)
            }
        }
}