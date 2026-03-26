package com.inspector.centinela.controller

import com.inspector.centinela.registry.TransformerRegistry
import com.inspector.centinela.registry.ValidatorRegistry
import java.lang.reflect.Field
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

/**
 * Processes transformation annotations on a specific field.
 *
 * It iterates through all annotations of the field, finds the corresponding [com.inspector.centinela.handler.DataTransformer]
 * from the [TransformerRegistry], and applies the transformation if the property is mutable.
 *
 * @param property The [Field] to be processed.
 * @param kClass The instance of the object containing the field.
 */
fun getTransformer(
    property: Field,
    kClass: Any
) {
    for (annotation in property.annotations) {

        val transformer = TransformerRegistry.getTransformer(annotation.annotationClass)

        if (transformer != null) {
            val result = transformer.transform(annotation, property.get(kClass))
            val kProp = kClass::class.memberProperties.find { it.name == property.name }
            if (result != property.get(kClass) && kProp is KMutableProperty<*>) {
                property.set(kClass, result)
            }
        }
    }
}

/**
 * Processes validation annotations on a specific field.
 *
 * It iterates through all annotations of the field, finds the corresponding [com.inspector.centinela.handler.ConstraintValidator]
 * from the [ValidatorRegistry], and performs validation. If a validation fails,
 * an [Exception] is added to the provided errors list.
 *
 * @param property The [Field] to be validated.
 * @param kClass The instance of the object containing the field.
 * @param errors A mutable list where validation exceptions will be collected.
 */
fun getValidator(
    property: Field,
    kClass: Any,
    errors: MutableList<Exception>
) {
    for (annotation in property.annotations) {
        val validator = ValidatorRegistry.getValidator(annotation.annotationClass)

        if (validator != null) {
            val isValid = validator.isValid(annotation, property.get(kClass))

            if (!isValid) {
                errors.add(Exception("Property '${property.name}' does not satisfy rule @${annotation.annotationClass.simpleName}"))
            }
        }
    }
}
