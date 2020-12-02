package com.casper.compiler.lexer.token

data class Token(
    val tokenType: TokenType,
    val lexeme: String,
    val line: Int
)
