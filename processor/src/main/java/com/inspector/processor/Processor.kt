package com.inspector.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.inspector.processor.rules.generator.ruleGeneratorsRegister
import com.inspector.processor.rules.generator.transformerGeneratorsRegister
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * A KSP (Kotlin Symbol Processing) processor that generates static validators and transformers
 * for classes containing inspector annotations.
 *
 * It generates an extension file with `validate`, `validateOrThrow`, and `transform` functions.
 *
 * @property logger The KSP logger for reporting errors and warnings.
 * @property codeGenerator The KSP code generator for creating new source files.
 */
class Processor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator
) : SymbolProcessor {

    /**
     * Entry point for the symbol processing round.
     *
     * Scans for all classes that have properties with annotations and triggers
     * the generation of the corresponding validator extension.
     *
     * @param resolver The resolver used to access symbols.
     * @return A list of deferred symbols.
     */
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val files = resolver.getNewFiles()

        for (file in files) {
            val classes = file.declarations.filterIsInstance<KSClassDeclaration>()

            for (ksClass in classes) {
                val annotatedProperties = ksClass.getAllProperties()
                    .filter { it.annotations.iterator().hasNext() }
                    .toList()

                if (annotatedProperties.isNotEmpty()) {
                    generateStaticValidator(ksClass, annotatedProperties)
                }
            }
        }
        return emptyList()
    }

    /**
     * Generates a Kotlin file containing extension functions for validation and transformation
     * using KotlinPoet.
     *
     * @param ksClass The class declaration to generate the validator for.
     * @param properties The list of annotated properties within the class.
     */
    private fun generateStaticValidator(ksClass: KSClassDeclaration, properties: List<KSPropertyDeclaration>) {

        val packageName = ksClass.packageName.asString()
        val className = ksClass.simpleName.asString()

        val classType = ClassName(packageName, className)

        val funBuilder = FunSpec.builder("validate")
            .receiver(classType)
            .returns(List::class.asClassName().parameterizedBy(String::class.asClassName()))
            .addStatement("val errors = mutableListOf<String>()")

        val sanitizeBuilder = FunSpec.builder("transform")
            .receiver(classType)
            .returns(classType)

        var containsTransforms = false

        for (prop in properties) {
            val propName = prop.simpleName.asString()

            for (annotation in prop.annotations) {
                val ruleName = annotation.shortName.asString()

                if (transformerGeneratorsRegister.containsKey(ruleName)) {

                    if (!prop.isMutable) {
                        logger.error(
                            "\n Processor error! The annotation @$ruleName modifies data, " +
                                    "so it can only be used on 'var' properties. " +
                                    "\n It was found on '$propName', which is declared as 'val'. " +
                                    "Change it to 'var' or remove the annotation.",
                            prop
                        )
                        continue
                    }

                    transformerGeneratorsRegister[ruleName]?.generateTransform(sanitizeBuilder, propName, annotation)
                    containsTransforms = true
                }
                if (ruleGeneratorsRegister.containsKey(ruleName)) {
                    ruleGeneratorsRegister[ruleName]?.generateCondition(funBuilder, propName, annotation)
                }
            }
        }

        funBuilder.addStatement("return errors")
        sanitizeBuilder.addStatement("return this")

        val exceptionClass = ClassName("com.inspector.centinela.model.exceptions", "ValidationAggregatorException")
        val throwFunBuilder = FunSpec.builder("validateOrThrow")
            .receiver(classType)
            .addStatement("val errors = this.validate()")
            .beginControlFlow("if (errors.isNotEmpty())")
            .addStatement("throw %T(errors)", exceptionClass)
            .endControlFlow()

        val fileBuilder = FileSpec.builder(packageName, "${className}Validator")
            .addFunction(funBuilder.build())
            .addFunction(throwFunBuilder.build())

        if (containsTransforms) {
            fileBuilder.addFunction(sanitizeBuilder.build())
        }

        fileBuilder.build().writeTo(codeGenerator, Dependencies(aggregating = false, ksClass.containingFile!!))
    }
}
