package com.casper.compiler.lexer

import com.casper.compiler.lexer.token.Token
import com.casper.compiler.lexer.token.TokenType

class Lexer(private val sourceCode: String) {

    private var startLexemePosition = 0
    private var currentPosition = 0
    private var currentLine = 1

    fun scanTokens(): List<Token> {
        val tokens = mutableListOf<Token>()

        while (notAtEndOfCode()) {
            startLexemePosition = currentPosition
            val token = scanToken()
            token?.let(tokens::add)
        }

        tokens.add(Token(TokenType.EOF, "", currentLine))
        return tokens
    }

    private fun scanToken(): Token? {
        val char = getNextCharacter()

        return when {
            char.isWhiteSpaceCharacterToken() -> createToken(TokenType.WHITE_SPACE_CHARACTER)
            char.isLineBreakCharacterToken() -> {
                currentLine++
                createToken(TokenType.LINE_BREAK_CHARACTER)
            }
            char.isControlCharacter() -> null
            else -> createToken(TokenType.TEXT_CHARACTER)
        }
    }

    private fun getNextCharacter(): Char {
        currentPosition++
        return sourceCode[currentPosition - 1]
    }

    private fun Char.isWhiteSpaceCharacterToken(): Boolean {
        return this == '\t' || this == ' '
    }

    private fun Char.isLineBreakCharacterToken(): Boolean {
        return this == '\u000A' || (this == '\u000D' && nextCharacterMatchLineFeed())
    }

    private fun nextCharacterMatchLineFeed(): Boolean {
        if (atEndOfCode()) return false
        if (sourceCode[currentPosition] != '\u000A') return false

        currentPosition++
        return true
    }

    private fun atEndOfCode() = !notAtEndOfCode()

    private fun notAtEndOfCode() = currentPosition < sourceCode.length

    private fun Char.isControlCharacter(): Boolean {
        return this < '\u0021'
    }

    private fun createToken(tokenType: TokenType): Token {
        return Token(
            tokenType,
            lexeme = sourceCode.substring(startLexemePosition until currentPosition),
            currentLine
        )
    }

}
