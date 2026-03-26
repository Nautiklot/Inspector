package com.inspector.processor

import com.google.auto.service.AutoService
import com.inspector.annotations.AllowedType
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypesException
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_17) // Usa la versión de Java de tu proyecto
@SupportedAnnotationTypes("com.inspector.annotations.*") // La ruta exacta de tu anotación
class ValidationProcessor : AbstractProcessor() {

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (annotations.isEmpty()) return false

        for (annotationElement in annotations) {
            val allowedTypeMeta = annotationElement.getAnnotation(AllowedType::class.java)

            if (allowedTypeMeta != null) {
                // 1. Ahora obtenemos una LISTA de nombres permitidos
                val expectedTypeNames = getExpectedTypeNames(allowedTypeMeta)

                // 2. Verificamos si Any::class (java.lang.Object) está en la lista
                val isAnyAllowed = expectedTypeNames.contains("java.lang.Object")

                val elementsToValidate = roundEnv.getElementsAnnotatedWith(annotationElement)

                for (element in elementsToValidate) {
                    if (element.kind == ElementKind.FIELD) {

                        val actualType = element.asType().toString()

                        // 3. Lógica de validación actualizada
                        // Si "Any" no está permitido, Y el tipo actual no está en la lista de permitidos... ¡Error!
                        if (!isAnyAllowed && !expectedTypeNames.contains(actualType)) {

                            // Unimos la lista para que el error se lea bonito (ej. "[int, double]")
                            val allowed = expectedTypeNames.joinToString(", ")

                            processingEnv.messager.printMessage(
                                Diagnostic.Kind.ERROR,
                                "\n Error: La anotación @${annotationElement.simpleName} " +
                                        "solo acepta los tipos: $allowed. " +
                                        "\n En el campo $element es de tipo '$actualType'. ",
                                element
                            )
                        }
                    }
                }
            }
        }
        return true
    }

    // --- NUEVA FUNCIÓN SALVAVIDAS (Para arreglos) ---
    private fun getExpectedTypeNames(annotation: AllowedType): List<String> {
        try {
            // Intento directo (rara vez funciona en tiempo de compilación)
            return annotation.types.map { it.java.name }
        } catch (e: MirroredTypesException) {
            // Extracción segura a través de los TypeMirrors
            return e.typeMirrors.map { it.toString() }
        }
    }
}