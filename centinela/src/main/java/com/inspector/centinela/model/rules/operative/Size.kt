package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.Size
import com.inspector.centinela.handler.ConstraintValidator
import java.lang.reflect.Field

// Validador para @Size
class SizeValidator : ConstraintValidator<Size, Any> {
    override fun isValid(annotation: Size, value: Any?): Boolean {
        if (value == null) return false
        return value.toString().length in annotation.min..annotation.max
    }
}