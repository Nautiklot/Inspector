package com.inspector.centinela.model.exceptions

// Excepción que agrupa todos los errores encontrados
class ValidationAggregatorException(val errors: List<Exception>) :
    Exception("Se encontraron ${errors.size} errores de validación.")