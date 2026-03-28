package com.inspector.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType

/**
 * A KSP (Kotlin Symbol Processing) processor that validates the usage of custom annotations.
 *
 * This processor scans for annotations that are meta-annotated with `AllowedType`.
 * It ensures that these annotations are only applied to properties whose types are
 * compatible with the types specified in the `AllowedType` declaration.
 *
 * @property logger The KSP logger used to report compilation errors and warnings.
 */
class Processor(
    private val logger: KSPLogger
) : SymbolProcessor {

    /**
     * Processes the source code symbols to validate annotation usage.
     *
     * It iterates through new files, class declarations, and their properties.
     * For each property, it checks its annotations for the presence of the `AllowedType` meta-annotation.
     * If found, it verifies if the property's type is assignable from any of the allowed types.
     *
     * @param resolver The resolver used to access symbols and type information.
     * @return A list of deferred symbols (always empty in this implementation).
     */
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val files = resolver.getNewFiles()
        for (file in files) {
            val classes = file.declarations.filterIsInstance<KSClassDeclaration>()
            for (ksClass in classes) {
                for (property in ksClass.getAllProperties()) {
                    for (annotation in property.annotations) {
                        val annotationType = annotation.annotationType.resolve()
                        val annotationDecl = annotationType.declaration
                        val allowedTypeAnnotation = annotationDecl.annotations.firstOrNull {
                            it.shortName.asString() == "AllowedType"
                        }

                        if (allowedTypeAnnotation != null) {
                            val typesArgument = allowedTypeAnnotation.arguments.first { it.name?.asString() == "types" }
                            
                            @Suppress("UNCHECKED_CAST")
                            val allowedTypes = typesArgument.value as? List<KSType> ?: continue
                            
                            val propertyType = property.type.resolve()
                            val isTypeAllowed = allowedTypes.any { allowedType ->
                                allowedType.isAssignableFrom(propertyType)
                            }

                            if (!isTypeAllowed) {
                                val allowedNames = allowedTypes.joinToString { it.declaration.simpleName.asString() }
                                val actualName = propertyType.declaration.simpleName.asString()
                                val ruleName = annotationDecl.simpleName.asString()

                                logger.error(
                                    "The annotation @$ruleName only accepts the types: $allowedNames. " +
                                            "\n An actual type of $actualName was found called '${property.simpleName.asString()}'.",
                                    property
                                )
                            }
                        }
                    }
                }
            }
        }
        return emptyList()
    }
}
