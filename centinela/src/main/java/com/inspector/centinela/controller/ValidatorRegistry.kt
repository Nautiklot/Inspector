package com.inspector.centinela.controller

import com.inspector.centinela.registry.TransformerRegistry
import com.inspector.centinela.registry.ValidatorRegistry
import com.inspector.centinela.utils.isMutable
import java.lang.reflect.Field
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

fun getTransformer(
    property: Field,
    kClass: Any
) {
    for (annotation in property.annotations) {

        val transformer = TransformerRegistry.getTransformer(annotation.annotationClass)

        if (transformer != null) {
            val result = transformer.transform(annotation, property.get(kClass))
            val kProp = kClass::class.memberProperties.find { it.name == property.name }
            if (result != property.get(kClass) &&  kProp is KMutableProperty<*>) {
                property.set(kClass, result)
            }
        }
    }
}

fun getValidator(
    property: Field,
    kClass: Any,
    errors: MutableList<Exception>
){
    for (annotation in property.annotations) {
        // Buscamos si tenemos un validador registrado para esta anotación
        val validator = ValidatorRegistry.getValidator(annotation.annotationClass)

        if (validator != null) {
            // Ejecutamos la validación
            val isValid = validator.isValid(annotation, property.get(kClass))

            if (!isValid) {
                // Aquí podrías personalizar el mensaje dependiendo de la anotación
                errors.add(Exception("La propiedad '${property.name}' no cumple la regla @${annotation.annotationClass.simpleName}"))
            }
        }
    }
}