package com.inspector.centinela.handler

/**
 * Interface for validating a value against a specific annotation constraint.
 *
 * @param A The type of the annotation (e.g., NotBlank).
 * @param T The type of the data to validate (e.g., String).
 */
interface ConstraintValidator<out A : Annotation, T> {
    /**
     * Checks if the given value satisfies the constraint defined by the annotation.
     *
     * @param annotation The annotation instance containing constraint parameters.
     * @param value The value to be validated.
     * @return True if the value is valid, false otherwise.
     */
    fun isValid(annotation: @UnsafeVariance A, value: T?): Boolean
}

/**
 * Interface for transforming a value based on an annotation.
 *
 * @param A The type of the annotation (e.g., Trim).
 * @param T The type of the data to transform.
 */
interface DataTransformer<out A : Annotation, T> {
    /**
     * Transforms the input value according to the rules of the annotation.
     *
     * @param annotation The annotation instance containing transformation parameters.
     * @param value The original value.
     * @return The transformed value.
     */
    fun transform(annotation: @UnsafeVariance A, value: T?): T?
}
