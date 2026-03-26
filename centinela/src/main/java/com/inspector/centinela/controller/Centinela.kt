package com.inspector.centinela.controller

import com.inspector.centinela.handler.ValidationResultHandler
import com.inspector.centinela.model.exceptions.ValidationAggregatorException

/**
 * Main entry point for the Inspector validation engine.
 *
 * This class coordinates the process of inspecting an object's fields, applying
 * transformations (like trimming or capitalization), and running validation rules.
 *
 * @property factory Configuration settings for the engine. If null, default settings are used.
 */
class Centinela(
    private val factory: CentinelaFactory? = CentinelaFactory()
) {

    /**
     * Processes the provided object by applying transformations and validations to its fields.
     *
     * @param target The object to inspect.
     * @param throws If true, throws a [ValidationAggregatorException] when validation errors are found and no handler is provided.
     * @param handler An optional [ValidationResultHandler] to process errors asynchronously or via callback.
     * @throws ValidationAggregatorException if [throws] is true, [handler] is null, and errors exist.
     */
    fun engine(target: Any, throws: Boolean = false, handler: ValidationResultHandler? = null) {
        val errors = mutableListOf<Exception>()
        val fields = target.javaClass.declaredFields

        fields.forEach { field ->
            // Ensure the field is accessible even if private
            field.isAccessible = true

            // Apply transformations if enabled
            if (factory?.transformerRegistry == true) {
                getTransformer(property = field, kClass = target)
            }

            // Apply validations if enabled
            if (factory?.validatorRegistry == true) {
                getValidator(property = field, kClass = target, errors = errors)
            }
        }

        // Handle collected errors
        if (errors.isNotEmpty()) {
            if (throws && handler == null) {
                throw ValidationAggregatorException(errors)
            } else {
                handler?.onValidationFailed(errors)
            }
        }
    }
}

/**
 * Configuration factory for the [Centinela] engine.
 *
 * @property transformerRegistry Enables or disables the application of [com.inspector.centinela.handler.DataTransformer]s.
 * @property validatorRegistry Enables or disables the application of [com.inspector.centinela.handler.ConstraintValidator]s.
 */
class CentinelaFactory(
    val transformerRegistry: Boolean = false,
    val validatorRegistry: Boolean = true,
)