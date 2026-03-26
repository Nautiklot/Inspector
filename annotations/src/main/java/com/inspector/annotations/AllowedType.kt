package com.inspector.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AllowedType(
    vararg val types: KClass<*>// Aquí guardamos si es String::class, Int::class, etc.
)