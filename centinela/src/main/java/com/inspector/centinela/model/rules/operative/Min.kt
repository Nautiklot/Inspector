package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.Min
import com.inspector.centinela.handler.ConstraintValidator
import java.lang.reflect.Field

// Validador para @Min
class MinValidator : ConstraintValidator<Min, Number> {
    override fun isValid(annotation: Min, value: Number?): Boolean {
        if (value == null) return false
        return value.toDouble() >= annotation.min
    }
}