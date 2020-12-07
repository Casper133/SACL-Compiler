package com.casper.compiler.library.error

import com.casper.compiler.library.token.Token
import com.casper.compiler.library.token.TokenType

var hadError = false

fun reportError(token: Token, message: String) {
    if (token.tokenType == TokenType.EOF) {
        reportError(token.line, "at end", message)
        return
    }

    reportError(token.line, "at '${token.lexeme}'", message)
}

private fun reportError(line: Int, location: String, message: String) {
    System.err.println("[Line $line] Error $location: $message.")
    hadError = true
}
