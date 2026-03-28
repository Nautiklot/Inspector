package com.inspector.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 * Provider class for [Processor].
 *
 * This class is responsible for instantiating the [SymbolProcessor] used by the
 * Kotlin Symbol Processing (KSP) engine.
 */
class ProcessorProvider : SymbolProcessorProvider {
    /**
     * Creates a new instance of [Processor].
     *
     * @param environment The environment providing KSP services like logger and options.
     * @return A new [SymbolProcessor] instance.
     */
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return Processor(
            logger = environment.logger
        )
    }
}
