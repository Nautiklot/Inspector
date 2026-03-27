package com.inspector.annotations

/**
 * Annotation to validate that a field is not null.
 * Applicable to any type.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(Any::class)
annotation class NotNull

/**
 * Annotation to validate that a numeric value is not zero.
 * Applicable to [Int], [Double], [Float], [Long], [Short], [Byte], and [Number].
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(Int::class, Double::class, Float::class, Long::class, Short::class, Byte::class, Number::class)
annotation class NotZero

/**
 * Annotation to validate that a string is not blank.
 * A string is considered blank if it is empty or consists only of whitespace characters.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(String::class)
annotation class NotBlank

/**
 * Annotation to validate that a string is not empty.
 * A string is empty if its length is 0.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(String::class)
annotation class NotEmpty

/**
 * Annotation to indicate that a string should be capitalized.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(String::class)
annotation class Capitalize

/**
 * Annotation to specify characters to be trimmed from a string.
 * @property chars The characters to trim. Defaults to a single space.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(String::class)
annotation class Trim(vararg val chars: Char = [' '])

/**
 * Annotation to validate the size or length of an element.
 * @property min The minimum size (inclusive).
 * @property max The maximum size (inclusive).
 */
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

/**
 * Annotation to validate a string against a regular expression.
 * @property regex The regular expression pattern to match.
 */
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

/**
 * Annotation to validate that a numeric value is positive (greater than zero).
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(Int::class, Long::class, Short::class, Byte::class, Float::class, Double::class)
annotation class Positive

/**
 * Annotation to validate that a numeric value is negative (less than zero).
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Repeatable
@AllowedType(Int::class, Long::class, Short::class, Byte::class, Float::class, Double::class)
annotation class Negative

/**
 * Annotation to validate that a numeric value is at least the specified minimum.
 * @property min The minimum allowed value.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@AllowedType(Int::class, Double::class, Float::class, Long::class, Short::class, Byte::class, Number::class)
annotation class Min(val min: Long = Long.MIN_VALUE)

/**
 * Annotation to validate that a numeric value is at most the specified maximum.
 * @property max The maximum allowed value.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@AllowedType(Int::class, Double::class, Float::class, Long::class, Short::class, Byte::class, Number::class)
annotation class Max(val max: Long = Long.MAX_VALUE)