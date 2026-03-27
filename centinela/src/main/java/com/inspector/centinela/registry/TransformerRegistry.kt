package com.inspector.centinela.registry

import com.inspector.annotations.Capitalize
import com.inspector.annotations.Trim
import com.inspector.centinela.handler.DataTransformer
import com.inspector.centinela.model.rules.operative.CapitalizeTransformer
import com.inspector.centinela.model.rules.operative.TrimTransformer
import kotlin.reflect.KClass

/**
 * A central registry that maps transformation annotations to their corresponding [DataTransformer] implementations.
 *
 * This object manages the initialization and retrieval of transformers for modifications
 * like [Trim], [Capitalize] etc.
 */
object TransformerRegistry {
    /**
     * Internal map storing the association between an annotation class and its transformer instance.
     */
    private val transformers = mutableMapOf<KClass<out Annotation>, DataTransformer<Annotation, *>>()

    init {
        transformers[Trim::class] = TrimTransformer()
        transformers[Capitalize::class] = CapitalizeTransformer()
    }

    /**
     * Retrieves the transformer associated with a specific annotation class.
     *
     * @param A The type of the annotation.
     * @param annotationClass The [KClass] of the annotation to find a transformer for.
     * @return The corresponding [DataTransformer], or null if no transformer is registered for the given class.
     */
    @Suppress("UNCHECKED_CAST")
    fun <A : Annotation> getTransformer(annotationClass: KClass<A>): DataTransformer<A, Any>? {
        return transformers[annotationClass] as? DataTransformer<A, Any>
    }
}
