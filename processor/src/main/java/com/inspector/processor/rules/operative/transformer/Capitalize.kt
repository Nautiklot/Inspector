package com.inspector.processor.rules.operative.transformer

import com.google.devtools.ksp.symbol.KSAnnotation
import com.inspector.processor.rules.generator.TransformerGenerator
import com.squareup.kotlinpoet.FunSpec

/**
 * A transformer generator for the [com.inspector.annotations.Capitalize] annotation.
 *
 * It generates the code necessary to capitalize the first character of a string property
 * during the code generation phase.
 */
class Capitalize : TransformerGenerator {
    /**
     * Generates a statement that calls `replaceFirstChar { it.uppercase() }` on the property
     * and assigns the result back to it.
     *
     * @param sanitizeBuilder The KotlinPoet builder for the transformation function.
     * @param propertyName The name of the property to be capitalized.
     * @param annotation The [KSAnnotation] instance for the Capitalize rule.
     */
    override fun generateTransform(sanitizeBuilder: FunSpec.Builder, propertyName: String, annotation: KSAnnotation) {
        sanitizeBuilder.addStatement("this.%L = this.%L.replaceFirstChar { it.uppercase() }", propertyName, propertyName)
    }
}
