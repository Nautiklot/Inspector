package com.inspector.centinela.utils

import kotlin.reflect.KMutableProperty

/**
 * Checks if a given property is mutable (can be changed after initialization).
 *
 * @param property The property to check, typically a [kotlin.reflect.KProperty].
 * @return True if the property is an instance of [KMutableProperty], false otherwise.
 */
fun isMutable(property: Any): Boolean {
    return property is KMutableProperty<*>
}
