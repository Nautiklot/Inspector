package com.inspector.centinela.utils

import kotlin.reflect.KMutableProperty

fun isMutable(property: Any): Boolean {
    return property is KMutableProperty<*>
}