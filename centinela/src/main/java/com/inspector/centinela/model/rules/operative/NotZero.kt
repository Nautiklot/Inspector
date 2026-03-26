package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.NotZero
import com.inspector.centinela.handler.ConstraintValidator
import java.lang.reflect.Field

// Validador para @NotZero
class NotZeroValidator : ConstraintValidator<NotZero, Number> {
    override fun isValid(annotation: NotZero, value: Number?): Boolean {
        if (value == null) return false
        return value.toDouble() != 0.0
    }
}