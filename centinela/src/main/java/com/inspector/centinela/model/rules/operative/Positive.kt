package com.inspector.centinela.model.rules.operative

import com.inspector.centinela.handler.ConstraintValidator
import com.inspector.annotations.Positive
import java.lang.reflect.Field

// Validador para @Positive
class PositiveValidator : ConstraintValidator<Positive, Number> {
    override fun isValid(annotation: Positive, value: Number?): Boolean {
        if (value == null) return false
        return value.toDouble() > 0.0
    }
}