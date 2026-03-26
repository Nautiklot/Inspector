package com.inspector.centinela.model.rules.operative

import com.inspector.centinela.handler.ConstraintValidator
import com.inspector.annotations.NotBlank
import java.lang.reflect.Field

// Validador para @NotBlank
class NotBlankValidator : ConstraintValidator<NotBlank, String> {
    override fun isValid(annotation: NotBlank, value: String?): Boolean {
        if (value == null) return false
        return value.isNotBlank()
    }
}