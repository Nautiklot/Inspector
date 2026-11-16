package com.inspector.processor.rules.operative.rule

import com.google.devtools.ksp.symbol.KSAnnotation
import com.inspector.processor.rules.generator.RuleGenerator
import com.squareup.kotlinpoet.FunSpec

/**
 * A rule generator for the [com.inspector.annotations.Capitalize] annotation.
 *
 * It generates the code necessary to validate that a string property starts with
 * an uppercase letter during the code generation phase.
 */
class Capitalize : RuleGenerator {

    /**
     * Generates a condition that checks if the first character of the property is uppercase.
     *
     * @param funBuilder The KotlinPoet builder for the validation function.
     * @param propertyName The name of the property to be validated.
     * @param annotation The [KSAnnotation] instance for the Capitalize rule.
     */
    override fun generateCondition(funBuilder: FunSpec.Builder, propertyName: String, annotation: KSAnnotation) {
        funBuilder.addStatement(
            "if (this.%L.isNotEmpty() && !this.%L.first().isUpperCase()) errors.add(%S)",
            propertyName,
            propertyName,
            "[$propertyName] must start with an uppercase letter."
        )
    }
}
