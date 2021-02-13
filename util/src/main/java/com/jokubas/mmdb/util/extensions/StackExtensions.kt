package com.jokubas.mmdb.util.extensions

import java.util.*

fun <T : Any> Stack<T>.popSafe(): T? = if (size > 0) {
    pop()
} else null