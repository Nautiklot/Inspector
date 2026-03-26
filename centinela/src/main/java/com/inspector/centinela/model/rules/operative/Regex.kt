package com.inspector.centinela.model.rules.operative

import com.inspector.centinela.handler.ConstraintValidator
import com.inspector.annotations.Regex
import java.lang.reflect.Field


// Validador para @Regex
class RegexValidator : ConstraintValidator<Regex, String> {
    override fun isValid(annotation: Regex, value: String?): Boolean {
        if (value == null) return false
        val regex = kotlin.text.Regex(annotation.regex)
        return regex.matches(value)
    }
}