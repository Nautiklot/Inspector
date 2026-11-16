package com.inspector.processor.rules.operative.rule

import com.google.devtools.ksp.symbol.KSAnnotation
import com.inspector.processor.rules.generator.RuleGenerator
import com.squareup.kotlinpoet.FunSpec

/**
 * A rule generator for the [com.inspector.annotations.Negative] annotation.
 *
 * It generates the code necessary to validate that a numeric property is strictly
 * less than zero during the code generation phase.
 */
class Negative : RuleGenerator {

    /**
     * Generates a condition that checks if the property value is negative.
     *
     * @param funBuilder The KotlinPoet builder for the validation function.
     * @param propertyName The name of the property to be validated.
     * @param annotation The [KSAnnotation] instance for the Negative rule.
     */
    override fun generateCondition(funBuilder: FunSpec.Builder, propertyName: String, annotation: KSAnnotation) {
        funBuilder.addStatement(
            "if (this.%L.toDouble() >= 0) errors.add(%S)",
            propertyName,
            "[$propertyName] must be less than zero."
        )
    }
}
