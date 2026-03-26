package com.inspector.centinela.model.rules.operative

import com.inspector.annotations.Trim
import com.inspector.centinela.handler.DataTransformer
import kotlin.Char
import kotlin.charArrayOf

class TrimTransformer : DataTransformer<Trim, String> {
    override fun transform(annotation: Trim, value: String?): String? {
        if (value == null) return null
        return if (annotation.chars.contentEquals(charArrayOf(' ')))
            value.trim()
        else value.trim(*annotation.chars)
    }
}