package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.NotNull
import com.inspector.centinela.handler.ConstraintValidator

/**
 * Validator for the [NotNull] annotation.
 *
 * It validates that a value is not null. This can be applied to any data type.
 */
class NotNullValidator : ConstraintValidator<NotNull, Any> {

    /**
     * Checks if the provided value is not null.
     *
     * @param annotation The [NotNull] annotation instance.
     * @param value The value to be validated.
     * @return True if the value is not null, false otherwise.
     */
    override fun isValid(annotation: NotNull, value: Any?): Boolean = value != null
}
