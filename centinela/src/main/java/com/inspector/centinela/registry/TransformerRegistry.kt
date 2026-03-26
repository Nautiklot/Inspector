package com.inspector.centinela.registry

import com.inspector.annotations.Capitalize
import com.inspector.annotations.Max
import com.inspector.annotations.Min
import com.inspector.annotations.Negative
import com.inspector.annotations.NotBlank
import com.inspector.annotations.NotEmpty
import com.inspector.annotations.NotNull
import com.inspector.annotations.NotZero
import com.inspector.annotations.Positive
import com.inspector.annotations.Regex
import com.inspector.annotations.Size
import com.inspector.annotations.Trim
import com.inspector.centinela.handler.ConstraintValidator
import com.inspector.centinela.handler.DataTransformer
import com.inspector.centinela.model.rules.operative.CapitalizeTransformer
import com.inspector.centinela.model.rules.operative.CapitalizeValidator
import com.inspector.centinela.model.rules.operative.MaxValidator
import com.inspector.centinela.model.rules.operative.MinValidator
import com.inspector.centinela.model.rules.operative.NegativeValidator
/*
import com.inspector.centinela.model.rules.operative.NotBlankValidator
import com.inspector.centinela.model.rules.operative.NotEmptyValidator
import com.inspector.centinela.model.rules.operative.NotNullValidator
import com.inspector.centinela.model.rules.operative.NotZeroValidator
import com.inspector.centinela.model.rules.operative.PositiveValidator
import com.inspector.centinela.model.rules.operative.RegexValidator
import com.inspector.centinela.model.rules.operative.SizeValidator

 */
import com.inspector.centinela.model.rules.operative.TrimTransformer

import kotlin.reflect.KClass

object TransformerRegistry {
    private val validators = mutableMapOf<KClass<out Annotation>, DataTransformer<Annotation, *>>()

    init {
        validators[Trim::class] = TrimTransformer()
        validators[Capitalize::class] = CapitalizeTransformer()
    }

    @Suppress("UNCHECKED_CAST")
    fun <A : Annotation> getTransformer(annotationClass: KClass<A>): DataTransformer<A, Any>? {
        return validators[annotationClass] as? DataTransformer<A, Any>
    }
}