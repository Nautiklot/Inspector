package com.inspector.annotations

import kotlin.reflect.KClass

/**
 * Meta-annotation used to specify the compatible data types for a custom validation annotation.
 *
 * This is used by the validation processor to ensure that an annotation is only applied
 * to fields of supported types (e.g., String, Int, etc.).
 *
 * @property types A variable number of [KClass] representing the allowed types.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AllowedType(
    vararg val types: KClass<*>
)