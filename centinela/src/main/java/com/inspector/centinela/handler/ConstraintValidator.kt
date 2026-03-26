package com.inspector.centinela.handler

import java.lang.reflect.Field

// A = El tipo de Anotación (ej. NotBlank)
// T = El tipo de dato que valida (ej. String)
interface ConstraintValidator<out A : Annotation, T> {
    fun isValid(annotation:  @UnsafeVariance A, value: T?): Boolean
}

// Recibe el valor y devuelve el valor modificado
interface DataTransformer<out A : Annotation, T> {
    fun transform(annotation: @UnsafeVariance A, value: T?): T?
}