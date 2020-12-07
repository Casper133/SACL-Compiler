package com.casper.compiler.library.token

data class Token(
    val tokenType: TokenType,
    val lexeme: String,
    val line: Int,
)
