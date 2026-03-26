package com.inspector.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(Any::class)
annotation class NotNull

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(Int::class, Double::class, Float::class, Long::class, Short::class, Byte::class, Number::class)
annotation class NotZero

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(String::class)
annotation class NotBlank

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(String::class)
annotation class NotEmpty

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(String::class)
annotation class Capitalize

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(String::class)
annotation class Trim(vararg val chars: Char = [' '])

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Repeatable
@AllowedType(Any::class)
annotation class Size(val min: Int = 0, val max: Int = Int.MAX_VALUE)

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Repeatable
@AllowedType(String::class)
annotation class Regex(val regex: String = "^[a-zA-Z0-9\\s]+$")

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(Int::class, Long::class, Short::class, Byte::class, Float::class, Double::class)
annotation class Positive

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(Int::class, Long::class, Short::class, Byte::class, Float::class, Double::class)
annotation class Negative

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@AllowedType(Int::class, Double::class, Float::class, Long::class, Short::class, Byte::class, Number::class)
annotation class Min(val min: Long = Long.MIN_VALUE)

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@AllowedType(Int::class, Double::class, Float::class, Long::class, Short::class, Byte::class, Number::class)
annotation class Max(val max: Long = Long.MAX_VALUE)