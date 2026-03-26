package com.inspector.centinela.handler

/**
 * Interface for handling the results of a validation process.
 *
 * Implementations of this interface can define custom behavior when
 * validation errors occur, such as logging, showing UI alerts, or throwing exceptions.
 */
interface ValidationResultHandler {
    /**
     * Called when one or more validation errors are found.
     *
     * @param errors A list of [Exception] objects representing the validation failures.
     */
    fun onValidationFailed(errors: List<Exception>)
}