package com.inspector.processor.rules.generator

import com.inspector.processor.rules.operative.rule.Capitalize
import com.inspector.processor.rules.operative.rule.Max
import com.inspector.processor.rules.operative.rule.Min
import com.inspector.processor.rules.operative.rule.Negative
import com.inspector.processor.rules.operative.rule.NotBlank
import com.inspector.processor.rules.operative.rule.NotEmpty
import com.inspector.processor.rules.operative.rule.NotNull
import com.inspector.processor.rules.operative.rule.NotZero
import com.inspector.processor.rules.operative.rule.Positive
import com.inspector.processor.rules.operative.rule.Regex
import com.inspector.processor.rules.operative.rule.Size

/**
 * Registry that maps annotation short names to their corresponding [RuleGenerator] implementations.
 *
 * This registry is used by the annotation processor to find the appropriate generator
 * for creating validation conditions during code generation.
 */
val ruleGeneratorsRegister : Map<String, RuleGenerator> = mapOf(
    "NotBlank" to NotBlank(),
    "NotEmpty" to NotEmpty(),
    "NotNull" to NotNull(),
    "Positive" to Positive(),
    "Negative" to Negative(),
    "NotZero" to NotZero(),
    "Min" to Min(),
    "Max" to Max(),
    "Size" to Size(),
    "Regex" to Regex(),
    "Capitalize" to Capitalize()
)
