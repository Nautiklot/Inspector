package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.Max
import com.inspector.centinela.handler.ConstraintValidator
import java.lang.reflect.Field

// Validador para @Max
class MaxValidator : ConstraintValidator<Max, Number> {
    override fun isValid(annotation: Max, value: Number?): Boolean {
        if (value == null) return false
        return value.toDouble() <= annotation.max
    }
}