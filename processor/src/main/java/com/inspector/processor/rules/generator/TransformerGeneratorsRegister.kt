package com.inspector.processor.rules.generator

import com.inspector.processor.rules.operative.transformer.Capitalize
import com.inspector.processor.rules.operative.transformer.Trim

/**
 * Registry that maps transformation annotation short names to their corresponding [TransformerGenerator] implementations.
 *
 * This registry is used by the annotation processor to find the appropriate generator
 * for creating data transformation logic during code generation.
 */
val transformerGeneratorsRegister : Map<String, TransformerGenerator> = mapOf(
    "Trim" to Trim(),
    "Capitalize" to Capitalize()
)
