package com.casper.compiler.lexer

import com.casper.compiler.lexer.token.Token

class Lexer(private val sourceCode: String) {

    fun scanTokens(): List<Token> {
        println(sourceCode)
        return emptyList()
    }

}
