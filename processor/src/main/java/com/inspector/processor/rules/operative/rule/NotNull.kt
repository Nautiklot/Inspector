package com.inspector.processor.rules.operative.rule

import com.google.devtools.ksp.symbol.KSAnnotation
import com.inspector.processor.rules.generator.RuleGenerator
import com.squareup.kotlinpoet.FunSpec

/**
 * A rule generator for the [com.inspector.annotations.NotNull] annotation.
 *
 * It generates the code necessary to validate that a property's value is not null
 * during the code generation phase.
 */
class NotNull : RuleGenerator {

    /**
     * Generates a condition that checks if the property is null.
     *
     * @param funBuilder The KotlinPoet builder for the validation function.
     * @param propertyName The name of the property to be validated.
     * @param annotation The [KSAnnotation] instance for the NotNull rule.
     */
    override fun generateCondition(funBuilder: FunSpec.Builder, propertyName: String, annotation: KSAnnotation) {
        funBuilder.addStatement(
            "if (this.%L == null) errors.add(%S)",
            propertyName,
            "[$propertyName] cannot be null."
        )
    }
}
