package com.inspector.processor.rules.generator

import com.google.devtools.ksp.symbol.KSAnnotation
import com.squareup.kotlinpoet.FunSpec

/**
 * Interface for generating validation logic during code generation.
 *
 * Implementations of this interface are responsible for adding code statements
 * to a [FunSpec.Builder] that check if a property satisfies a specific validation rule.
 */
interface RuleGenerator {
    /**
     * Generates and adds a validation condition to the provided function builder.
     *
     * @param funBuilder The KotlinPoet builder for the validation function.
     * @param propertyName The name of the property being validated.
     * @param annotation The [KSAnnotation] instance containing the rule's parameters.
     */
    fun generateCondition(funBuilder: FunSpec.Builder, propertyName: String, annotation: KSAnnotation)
}
