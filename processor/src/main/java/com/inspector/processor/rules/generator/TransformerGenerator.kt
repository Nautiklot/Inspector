package com.inspector.processor.rules.generator

import com.google.devtools.ksp.symbol.KSAnnotation
import com.squareup.kotlinpoet.FunSpec

/**
 * Interface for generating data transformation logic during code generation.
 *
 * Implementations of this interface are responsible for adding code statements
 * to a [FunSpec.Builder] that modify or "sanitize" a property's value based
 * on a specific transformation annotation.
 */
interface TransformerGenerator {
    /**
     * Generates and adds transformation logic to the provided function builder.
     *
     * @param sanitizeBuilder The KotlinPoet builder for the transformation (sanitize) function.
     * @param propertyName The name of the property being transformed.
     * @param annotation The [KSAnnotation] instance containing the transformation parameters.
     */
    fun generateTransform(sanitizeBuilder: FunSpec.Builder, propertyName: String, annotation: KSAnnotation)
}
