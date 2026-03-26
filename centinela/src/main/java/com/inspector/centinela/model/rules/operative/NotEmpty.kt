package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.NotEmpty
import com.inspector.centinela.handler.ConstraintValidator
import java.lang.reflect.Field

// Validador para @NotEmpty
class NotEmptyValidator : ConstraintValidator<NotEmpty, String> {
    override fun isValid(annotation: NotEmpty, value: String?): Boolean {
        if (value == null) return false
        return value.isNotEmpty()
    }
}