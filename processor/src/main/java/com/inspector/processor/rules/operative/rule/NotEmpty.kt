package com.inspector.processor.rules.operative.rule

import com.google.devtools.ksp.symbol.KSAnnotation
import com.inspector.processor.rules.generator.RuleGenerator
import com.squareup.kotlinpoet.FunSpec

/**
 * A rule generator for the [com.inspector.annotations.NotEmpty] annotation.
 *
 * It generates the code necessary to validate that a property's string representation
 * is not empty during the code generation phase.
 */
class NotEmpty : RuleGenerator {

    /**
     * Generates a condition that checks if the property's string representation is empty.
     *
     * @param funBuilder The KotlinPoet builder for the validation function.
     * @param propertyName The name of the property to be validated.
     * @param annotation The [KSAnnotation] instance for the NotEmpty rule.
     */
    override fun generateCondition(funBuilder: FunSpec.Builder, propertyName: String, annotation: KSAnnotation) {
        funBuilder.addStatement(
            "if (this.%L.toString().isEmpty()) errors.add(%S)",
            propertyName,
            "[$propertyName] cannot be empty."
        )
    }
}
