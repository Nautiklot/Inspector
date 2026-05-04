package com.inspector.processor.rules.operative.rule

import com.google.devtools.ksp.symbol.KSAnnotation
import com.inspector.processor.rules.generator.RuleGenerator
import com.squareup.kotlinpoet.FunSpec

/**
 * A rule generator for the [com.inspector.annotations.Max] annotation.
 *
 * It generates the code necessary to validate that a numeric property does not
 * exceed the maximum value specified in the annotation during the code generation phase.
 */
class Max : RuleGenerator {

    /**
     * Generates a condition that checks if the property value is less than or equal to the maximum.
     *
     * @param funBuilder The KotlinPoet builder for the validation function.
     * @param propertyName The name of the property to be validated.
     * @param annotation The [KSAnnotation] instance for the Max rule.
     */
    override fun generateCondition(funBuilder: FunSpec.Builder, propertyName: String, annotation: KSAnnotation) {
        val maxValue = annotation.arguments.firstOrNull { it.name?.asString() == "max" }?.value.toString()
        funBuilder.addStatement(
            "if (this.%L.toDouble() > %L) errors.add(%S)",
            propertyName,
            maxValue,
            "[$propertyName] must not exceed $maxValue."
        )
    }
}
