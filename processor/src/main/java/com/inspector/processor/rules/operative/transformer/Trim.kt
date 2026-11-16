package com.inspector.processor.rules.operative.transformer

import com.google.devtools.ksp.symbol.KSAnnotation
import com.inspector.processor.rules.generator.TransformerGenerator
import com.squareup.kotlinpoet.FunSpec

/**
 * A transformer generator for the [com.inspector.annotations.Trim] annotation.
 *
 * It generates the code necessary to trim leading and trailing whitespace from a string property
 * during the code generation phase.
 */
class Trim : TransformerGenerator {
    /**
     * Generates a statement that calls `trim()` on the property and assigns the result back to it.
     *
     * @param sanitizeBuilder The KotlinPoet builder for the transformation function.
     * @param propertyName The name of the property to be trimmed.
     * @param annotation The [KSAnnotation] instance for the Trim rule.
     */
    override fun generateTransform(sanitizeBuilder: FunSpec.Builder, propertyName: String, annotation: KSAnnotation) {
        sanitizeBuilder.addStatement("this.%L = this.%L.trim()", propertyName, propertyName)
    }
}
