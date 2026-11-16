package com.inspector.processor.rules.operative.rule

import com.google.devtools.ksp.symbol.KSAnnotation
import com.inspector.processor.rules.generator.RuleGenerator
import com.squareup.kotlinpoet.FunSpec

/**
 * A rule generator for the [com.inspector.annotations.Regex] annotation.
 *
 * It generates the code necessary to validate that a property's string representation
 * matches a specific regular expression pattern during the code generation phase.
 */
class Regex : RuleGenerator {

    /**
     * Generates a condition that checks if the property value matches the regex pattern.
     *
     * @param funBuilder The KotlinPoet builder for the validation function.
     * @param propertyName The name of the property to be validated.
     * @param annotation The [KSAnnotation] instance for the Regex rule.
     */
    override fun generateCondition(funBuilder: FunSpec.Builder, propertyName: String, annotation: KSAnnotation) {
        val pattern = annotation.arguments.firstOrNull { it.name?.asString() == "pattern" }?.value.toString()
        funBuilder.addStatement(
            "if (!Regex(%S).matches(this.%L.toString())) errors.add(%S)",
            pattern,
            propertyName,
            "[$propertyName] does not match the required format."
        )
    }
}
