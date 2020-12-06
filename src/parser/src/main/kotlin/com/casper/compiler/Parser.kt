package com.casper.compiler

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.impl.ConfigBlock
import com.casper.compiler.library.expression.impl.ConfigBlockBody
import com.casper.compiler.library.expression.impl.ConstantCall
import com.casper.compiler.library.expression.impl.ConstantDeclaration
import com.casper.compiler.library.expression.impl.ConstantsBlock
import com.casper.compiler.library.expression.impl.EscapedSequence
import com.casper.compiler.library.expression.impl.Identifier
import com.casper.compiler.library.expression.impl.RecordDeclaration
import com.casper.compiler.library.expression.impl.RecordValue
import com.casper.compiler.library.expression.impl.SourceCode
import com.casper.compiler.library.token.Token
import com.casper.compiler.library.token.TokenType

@Suppress("ControlFlowWithEmptyBody")
class Parser(private val tokens: List<Token>) {

    private var currentTokenIndex = 0
    private val identifierMatchTokens = arrayOf(
        TokenType.TEXT_CHARACTER, TokenType.C_LETTER, TokenType.O_LETTER,
        TokenType.N_LETTER, TokenType.S_LETTER, TokenType.T_LETTER,
        TokenType.LEFT_BRACE, TokenType.RIGHT_BRACE, TokenType.EQUALS,
        TokenType.DOLLAR_SIGN, TokenType.BACKSLASH
    )

    private val charactersSequenceMatchTokens = arrayOf(
        *identifierMatchTokens, TokenType.WHITE_SPACE_CHARACTER
    )

    fun parse(): Expression = sourceCode()

    private fun sourceCode(): Expression {
        while (matchLinesSeparator()) {
        }

        val constantsBlock = constantsBlock()

        if (!matchLinesSeparator()) {
            TODO("Throw syntax error")
        }

        while (matchLinesSeparator()) {
        }

        return SourceCode(
            constantsBlock,
            configBlockBody()
        )
    }

    private fun constantsBlock(): Expression? {
        val constantDeclarations = mutableListOf<Expression>()
        constantDeclarations.add(constantDeclaration() ?: return null)

        while (matchLinesSeparator()) {
            while (matchLinesSeparator()) {
            }

            constantDeclaration()?.also(constantDeclarations::add)
        }

        return ConstantsBlock(constantDeclarations)
    }

    private fun constantDeclaration(): Expression? {
        if (!matchConstKeyword()) {
            return null
        }

        // Advance const keyword
        for (i in 1..5) {
            advanceToken()
        }

        if (!matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
            TODO("Throw syntax error")
        }

        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }

        return ConstantDeclaration(
            recordDeclaration(
                extractIdentifierBeforeWhiteSpaceChars()
            )
        )
    }

    private fun matchConstKeyword(): Boolean {
        return currentTokenMatch(TokenType.C_LETTER)
                && lookaheadMatch(TokenType.O_LETTER, shift = 1)
                && lookaheadMatch(TokenType.N_LETTER, shift = 2)
                && lookaheadMatch(TokenType.S_LETTER, shift = 3)
                && lookaheadMatch(TokenType.T_LETTER, shift = 4)
    }

    private fun lookaheadMatch(tokenType: TokenType, shift: Int): Boolean =
        isNotEndOfTokensStream()
                && (currentTokenIndex + shift < tokens.size)
                && tokens[currentTokenIndex + shift].tokenType == tokenType

    private fun configBlockBody(): Expression {
        val bodyExpressions = mutableListOf<Expression>()
        bodyExpressions.add(extractConfigBodyExpression())

        while (matchLinesSeparator()) {
            while (matchLinesSeparator()) {
            }

            bodyExpressions.add(extractConfigBodyExpression())
        }

        return ConfigBlockBody(bodyExpressions)
    }

    private fun extractConfigBodyExpression(): Expression {
        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }

        val identifier = extractIdentifierBeforeWhiteSpaceChars()

        return when {
            currentTokenMatch(TokenType.LEFT_BRACE) -> configBlock(identifier)
            currentTokenMatch(TokenType.EQUALS) -> recordDeclaration(identifier)
            else -> TODO("Throw syntax error")
        }
    }

    private fun extractIdentifierBeforeWhiteSpaceChars(): Expression {
        val identifier = identifier()

        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }
        return identifier
    }

    private fun configBlock(identifier: Expression): Expression {
        if (!matchAndAdvanceToken(TokenType.LEFT_BRACE)) {
            TODO("Throw syntax error")
        }

        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }

        if (!matchLinesSeparator()) {
            TODO("Throw syntax error")
        }

        while (matchLinesSeparator()) {
        }

        val configBlockBody = configBlockBody()

        if (!matchLinesSeparator()) {
            TODO("Throw syntax error")
        }

        while (matchLinesSeparator()) {
        }

        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }

        if (!matchAndAdvanceToken(TokenType.RIGHT_BRACE)) {
            TODO("Throw syntax error")
        }

        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }

        return ConfigBlock(identifier, configBlockBody)
    }

    private fun matchLinesSeparator(): Boolean {
        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }

        return matchAndAdvanceToken(TokenType.LINE_BREAK_CHARACTER)
    }

    private fun recordDeclaration(identifier: Expression): Expression {
        if (!matchAndAdvanceToken(TokenType.EQUALS)) {
            TODO("Throw syntax error")
        }

        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }

        return RecordDeclaration(identifier, recordValue())
    }

    private fun recordValue(): Expression {
        return RecordValue(
            constantCall(),
            escapedSequence(),
            charactersSequence()
        )
    }

    private fun constantCall(): Expression? {
        return when {
            matchAndAdvanceToken(TokenType.DOLLAR_SIGN) -> ConstantCall(identifier())
            else -> null
        }
    }

    private fun identifier(): Expression {
        val identifierBuilder = StringBuilder()

        while (match(*identifierMatchTokens)) {
            identifierBuilder.append(advanceToken().lexeme)
        }

        if (identifierBuilder.isEmpty()) {
            TODO("Throw syntax error")
        }

        return Identifier(identifierBuilder.toString())
    }

    private fun escapedSequence(): Expression? {
        return when {
            matchAndAdvanceToken(TokenType.BACKSLASH) -> EscapedSequence(extractEscapedText())
            else -> null
        }
    }

    private fun extractEscapedText(): String {
        val escapedTextBuilder = StringBuilder().append(advanceToken().lexeme)

        while (matchAndAdvanceToken(TokenType.BACKSLASH)) {
            escapedTextBuilder.append(advanceToken().lexeme)
        }

        return escapedTextBuilder.toString()
    }

    private fun charactersSequence(): Expression? {
        val charactersSequenceBuilder = StringBuilder()

        while (match(*charactersSequenceMatchTokens)) {
            charactersSequenceBuilder.append(advanceToken().lexeme)
        }

        if (charactersSequenceBuilder.isEmpty()) {
            return null
        }

        return Identifier(charactersSequenceBuilder.toString())
    }

    private fun matchAndAdvanceToken(vararg tokenTypes: TokenType): Boolean =
        tokenTypes
            .firstOrNull(this::currentTokenMatch)
            ?.also { advanceToken() }
            ?.let { true }
            ?: false

    private fun match(vararg tokenTypes: TokenType): Boolean =
        tokenTypes
            .firstOrNull(this::currentTokenMatch)
            ?.let { true }
            ?: false

    private fun currentTokenMatch(tokenType: TokenType): Boolean =
        isNotEndOfTokensStream() && getCurrentToken().tokenType == tokenType

    private fun advanceToken(): Token =
        getCurrentToken()
            .also { if (isNotEndOfTokensStream()) currentTokenIndex++ }

    private fun isNotEndOfTokensStream(): Boolean =
        getCurrentToken().tokenType != TokenType.EOF

    private fun getCurrentToken(): Token = tokens[currentTokenIndex]

}
