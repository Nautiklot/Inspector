package com.inspector.centinela.handler


interface ValidationResultHandler {
    fun onValidationFailed(errors: List<Exception>)
}