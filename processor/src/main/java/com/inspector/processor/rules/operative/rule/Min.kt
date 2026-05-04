package com.inspector.processor.rules.operative.rule

import com.google.devtools.ksp.symbol.KSAnnotation
import com.inspector.processor.rules.generator.RuleGenerator
import com.squareup.kotlinpoet.FunSpec

/**
 * A rule generator for the [com.inspector.annotations.Min] annotation.
 *
 * It generates the code necessary to validate that a numeric property is at least
 * the minimum value specified in the annotation during the code generation phase.
 */
class Min : RuleGenerator {

    /**
     * Generates a condition that checks if the property value is greater than or equal to the minimum.
     *
     * @param funBuilder The KotlinPoet builder for the validation function.
     * @param propertyName The name of the property to be validated.
     * @param annotation The [KSAnnotation] instance for the Min rule.
     */
    override fun generateCondition(funBuilder: FunSpec.Builder, propertyName: String, annotation: KSAnnotation) {
        val minValue = annotation.arguments.firstOrNull { it.name?.asString() == "min" }?.value.toString()
        funBuilder.addStatement(
            "if (this.%L.toDouble() < %L) errors.add(%S)",
            propertyName,
            minValue,
            "[$propertyName] must be at least $minValue."
        )
    }
}
