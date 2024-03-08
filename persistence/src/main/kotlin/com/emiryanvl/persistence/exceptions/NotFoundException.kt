package com.emiryanvl.persistence.exceptions

import java.text.MessageFormat
import java.util.function.Supplier

class NotFoundException(message: String) : RuntimeException (message) {

    constructor(message: String, vararg args: Any?) : this(MessageFormat.format(message, *args))

    companion object {
        fun notFoundException(message: String, vararg args: Any?): Supplier<NotFoundException> {
            return Supplier<NotFoundException> { NotFoundException(message, *args) }
        }

        fun notFoundException(message: String): Supplier<NotFoundException> {
            return Supplier<NotFoundException> { NotFoundException(message) }
        }
    }
}