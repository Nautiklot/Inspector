package com.inspector.centinela.model.rules.operative

import com.inspector.centinela.handler.ConstraintValidator
import com.inspector.centinela.handler.DataTransformer
import com.inspector.annotations.Capitalize
import java.lang.reflect.Field

class CapitalizeTransformer : DataTransformer<Capitalize, String> {
    override fun transform(annotation: Capitalize, value: String?): String? {
        if (value == null) return null
        return value.replaceFirstChar {
            if (it.isLowerCase()) {
                it.titlecase()
            } else {
                it.toString()
            }
        }
    }
}

class CapitalizeValidator : ConstraintValidator<Capitalize, String> {
    override fun isValid(annotation: Capitalize, value: String?): Boolean {
        if (value == null) return false
        value.trim()
        if (!value.isBlank()) {
            if (value.first().isUpperCase()){
                return true
            }
        }
        return false
    }
}