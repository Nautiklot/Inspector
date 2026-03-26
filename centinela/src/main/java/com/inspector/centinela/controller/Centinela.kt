package com.inspector.centinela.controller

import com.inspector.centinela.handler.ValidationResultHandler
import com.inspector.centinela.model.exceptions.ValidationAggregatorException

class Centinela(
    val centinelaFactory: CentinelaFactory?= null
) {

    fun engine(any: Any, throws: Boolean, handler: ValidationResultHandler? = null) {
        val errors = mutableListOf<Exception>()
        val fields = any.javaClass.declaredFields

        for (property in fields.indices) {
            fields[property].isAccessible = true
            if (centinelaFactory?.transformerRegistry == true) getTransformer(property = fields[property], kClass = any)
            if (centinelaFactory?.validatorRegistry == true) getValidator(property = fields[property], kClass = any, errors = errors)
        }
        // Lógica de retorno que definimos anteriormente
        if (errors.isNotEmpty()) {
            if (throws && handler == null) {
                throw ValidationAggregatorException(errors)
            }
            else {
                handler?.onValidationFailed(errors)
            }
        }
    }
}

class CentinelaFactory(
    val transformerRegistry: Boolean = false,
    val validatorRegistry: Boolean = true,
)