package com.casper.compiler.library.error

import com.casper.compiler.library.token.Token
import com.casper.compiler.library.token.TokenType

var hadError = false

fun reportError(token: Token, message: String) {
    if (token.tokenType == TokenType.EOF) {
        reportError(token.line, "at the end of file", message)
        return
    }

    if (token.tokenType == TokenType.LINE_BREAK_CHARACTER) {
        reportError(token.line, "at the end of the line", message)
        return
    }

    reportError(token.line, "at '${token.lexeme}'", message)
}

fun reportError(line: Int, message: String) {
    reportError(line, location = "", message)
}

fun reportError(lines: List<Int>, message: String) {
    val linesString = lines.joinToString { it.toString() }
    System.err.println("[Lines $linesString] Error: $message.")
    hadError = true
}

private fun reportError(line: Int, location: String, message: String) {
    val errorPrefix = if (location.isEmpty()) "Error" else "Error $location"

    System.err.println("[Line $line] $errorPrefix: $message.")
    hadError = true
}

fun reportCriticalError(message: String) {
    System.err.println("Critical error: $message")
}
