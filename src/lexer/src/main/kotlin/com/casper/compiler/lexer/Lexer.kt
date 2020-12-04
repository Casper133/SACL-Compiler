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
            char.isLineBreakCharacterToken() -> createToken(TokenType.LINE_BREAK_CHARACTER).also { currentLine++ }
            char.isControlCharacter() -> null
            char == 'c' -> createToken(TokenType.C_LETTER)
            char == 'o' -> createToken(TokenType.O_LETTER)
            char == 'n' -> createToken(TokenType.N_LETTER)
            char == 's' -> createToken(TokenType.S_LETTER)
            char == 't' -> createToken(TokenType.T_LETTER)
            char == '{' -> createToken(TokenType.LEFT_BRACE)
            char == '}' -> createToken(TokenType.RIGHT_BRACE)
            char == '=' -> createToken(TokenType.EQUALS)
            char == '$' -> createToken(TokenType.DOLLAR_SIGN)
            char == '\\' -> createToken(TokenType.BACKSLASH)
            else -> createToken(TokenType.TEXT_CHARACTER)
        }
    }

    private fun getNextCharacter(): Char = currentPosition++.let { sourceCode[currentPosition - 1] }

    private fun Char.isControlCharacter(): Boolean = this < '\u0021'

    private fun Char.isWhiteSpaceCharacterToken(): Boolean = this == '\t' || this == ' '

    private fun Char.isLineBreakCharacterToken(): Boolean =
        this == '\u000A' || (this == '\u000D' && nextCharacterMatchLineFeed()) || this == '\u000D'

    private fun nextCharacterMatchLineFeed(): Boolean {
        if (atEndOfCode()) return false
        if (sourceCode[currentPosition] != '\u000A') return false

        currentPosition++
        return true
    }

    private fun atEndOfCode() = !notAtEndOfCode()

    private fun notAtEndOfCode() = currentPosition < sourceCode.length

    private fun createToken(tokenType: TokenType): Token =
        Token(
            tokenType,
            lexeme = sourceCode.substring(startLexemePosition until currentPosition),
            currentLine
        )

}
