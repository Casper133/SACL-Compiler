package com.casper.compiler.library

var hadError = false

fun reportError(line: Int, message: String) {
    System.err.println("[Line $line] Error: $message.")
    hadError = true
}
