package com.inspector.centinela.model.exceptions


class ValidationAggregatorException(val errors: List<String>) :
    Exception("Found ${errors.size} validation errors. Details: ${errors.joinToString("\n")}")