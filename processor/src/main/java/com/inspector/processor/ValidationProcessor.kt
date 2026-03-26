package com.inspector.processor

import com.google.auto.service.AutoService
import com.inspector.annotations.AllowedType
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypesException
import javax.tools.Diagnostic

/**
 * Annotation processor that validates if custom validation annotations are applied to compatible data types.
 *
 * It checks for the presence of the [AllowedType] meta-annotation on the applied annotations
 * and verifies if the field type matches any of the allowed types.
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("com.inspector.annotations.*")
class ValidationProcessor : AbstractProcessor() {

    /**
     * Processes the set of annotations and validates the elements they are applied to.
     *
     * @param annotations The set of annotations to process.
     * @param roundEnv The environment for information about the current and prior round.
     * @return True if the annotations are claimed by this processor, false otherwise.
     */
    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (annotations.isEmpty()) return false

        for (annotationElement in annotations) {
            val allowedTypeMeta = annotationElement.getAnnotation(AllowedType::class.java)

            if (allowedTypeMeta != null) {
                val expectedTypeNames = getExpectedTypeNames(allowedTypeMeta)

                val isAnyAllowed = expectedTypeNames.contains("java.lang.Object")

                val elementsToValidate = roundEnv.getElementsAnnotatedWith(annotationElement)

                for (element in elementsToValidate) {
                    if (element.kind == ElementKind.FIELD) {

                        val actualType = element.asType().toString()

                        if (!isAnyAllowed && !expectedTypeNames.contains(actualType)) {

                            val allowed = expectedTypeNames.joinToString(", ")

                            processingEnv.messager.printMessage(
                                Diagnostic.Kind.ERROR,
                                "\n Error: The annotation @${annotationElement.simpleName} " +
                                        "only accepts the types: $allowed. " +
                                        "\n In the field $element it is of type '$actualType'. ",
                                element
                            )
                        }
                    }
                }
            }
        }
        return true
    }

    /**
     * Extracts the fully qualified names of the classes specified in the [AllowedType] annotation.
     *
     * Handles [MirroredTypesException] to safely access class information during the processing phase.
     *
     * @param annotation The [AllowedType] annotation instance.
     * @return A list of strings representing the names of the allowed types.
     */
    private fun getExpectedTypeNames(annotation: AllowedType): List<String> {
        try {
            return annotation.types.map { it.java.name }
        } catch (e: MirroredTypesException) {
            return e.typeMirrors.map { it.toString() }
        }
    }
}