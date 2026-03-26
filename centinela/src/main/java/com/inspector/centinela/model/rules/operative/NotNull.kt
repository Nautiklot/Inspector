package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.NotNull
import com.inspector.centinela.handler.ConstraintValidator
import java.lang.reflect.Field

// Validador para @NotNull
class NotNullValidator : ConstraintValidator<NotNull, Any> {
    override fun isValid(annotation: NotNull, value: Any?): Boolean {
        return if (value != null) return true else false
    }
}