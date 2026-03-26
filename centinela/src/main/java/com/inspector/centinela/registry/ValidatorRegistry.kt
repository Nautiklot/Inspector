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
import com.inspector.centinela.handler.ConstraintValidator
import com.inspector.centinela.model.rules.operative.CapitalizeValidator
import com.inspector.centinela.model.rules.operative.MaxValidator
import com.inspector.centinela.model.rules.operative.MinValidator
import com.inspector.centinela.model.rules.operative.NegativeValidator
import com.inspector.centinela.model.rules.operative.NotBlankValidator
import com.inspector.centinela.model.rules.operative.NotEmptyValidator
import com.inspector.centinela.model.rules.operative.NotNullValidator
import com.inspector.centinela.model.rules.operative.NotZeroValidator
import com.inspector.centinela.model.rules.operative.PositiveValidator
import com.inspector.centinela.model.rules.operative.RegexValidator
import com.inspector.centinela.model.rules.operative.SizeValidator
import kotlin.reflect.KClass

/**
 * A central registry that maps validation annotations to their corresponding [ConstraintValidator] implementations.
 *
 * This object manages the initialization and retrieval of validators for various constraints
 * like [NotNull], [NotEmpty], [Positive], etc.
 */
object ValidatorRegistry {
    /**
     * Internal map storing the association between an annotation class and its validator instance.
     */
    private val validators = mutableMapOf<KClass<out Annotation>, ConstraintValidator<Annotation, *>>()

    init {
        validators[Capitalize::class] = CapitalizeValidator()
        validators[NotEmpty::class] = NotEmptyValidator()
        validators[NotBlank::class] = NotBlankValidator()
        validators[Positive::class] = PositiveValidator()
        validators[Negative::class] = NegativeValidator()
        validators[Size::class] = SizeValidator()
        validators[NotNull::class] = NotNullValidator()
        validators[NotZero::class] = NotZeroValidator()
        validators[Regex::class] = RegexValidator()
        validators[Min::class] = MinValidator()
        validators[Max::class] = MaxValidator()
    }

    /**
     * Retrieves the validator associated with a specific annotation class.
     *
     * @param A The type of the annotation.
     * @param annotationClass The [KClass] of the annotation to find a validator for.
     * @return The corresponding [ConstraintValidator], or null if no validator is registered for the given class.
     */
    @Suppress("UNCHECKED_CAST")
    fun <A : Annotation> getValidator(annotationClass: KClass<A>): ConstraintValidator<A, Any>? {
        return validators[annotationClass] as? ConstraintValidator<A, Any>
    }
}