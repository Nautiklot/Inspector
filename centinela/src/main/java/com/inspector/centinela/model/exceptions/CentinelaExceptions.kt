package com.inspector.centinela.model.exceptions

/**
 * Exception that aggregates all validation errors found during a validation process.
 *
 * @property errors A list of [Exception] objects representing each validation failure.
 */
class ValidationAggregatorException(val errors: List<Exception>) :
    Exception("Found ${errors.size} validation errors.")