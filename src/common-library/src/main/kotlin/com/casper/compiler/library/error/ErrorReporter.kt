package com.casper.compiler.library.error

var hadError = false

fun reportError(line: Int, message: String) {
    System.err.println("[Line $line] Error: $message.")
    hadError = true
}
