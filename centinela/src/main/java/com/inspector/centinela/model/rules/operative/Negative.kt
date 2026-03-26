package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.Negative
import com.inspector.centinela.handler.ConstraintValidator
import java.lang.reflect.Field

// Validador para @Negative
class NegativeValidator : ConstraintValidator<Negative, Number> {
    override fun isValid(annotation: Negative, value: Number?): Boolean {
        if (value == null) return false
        return value.toDouble() < 0.0
    }
}