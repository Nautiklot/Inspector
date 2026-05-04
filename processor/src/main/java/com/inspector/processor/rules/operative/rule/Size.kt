package com.inspector.processor.rules.operative.rule

import com.google.devtools.ksp.symbol.KSAnnotation
import com.inspector.processor.rules.generator.RuleGenerator
import com.squareup.kotlinpoet.FunSpec

/**
 * A rule generator for the [com.inspector.annotations.Size] annotation.
 *
 * It generates the code necessary to validate that the string representation of a property's
 * value has a length within the specified min and max range.
 */
class Size : RuleGenerator {

    /**
     * Generates a condition that checks if the length of the property's string representation
     * is within the allowed range.
     *
     * @param funBuilder The KotlinPoet builder for the validation function.
     * @param propertyName The name of the property to be validated.
     * @param annotation The [KSAnnotation] instance for the Size rule.
     */
    override fun generateCondition(funBuilder: FunSpec.Builder, propertyName: String, annotation: KSAnnotation) {
        val min = annotation.arguments.firstOrNull { it.name?.asString() == "min" }?.value.toString()
        val max = annotation.arguments.firstOrNull { it.name?.asString() == "max" }?.value.toString()
        funBuilder.addStatement(
            "if (this.%L.toString().length !in %L..%L) errors.add(%S)",
            propertyName,
            min,
            max,
            "[$propertyName] must have between $min and $max characters."
        )
    }
}
