package com.casper.compiler

import com.casper.compiler.exception.ParseException
import com.casper.compiler.library.error.reportError
import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.impl.CharactersSequence
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

// TODO: Refactor parser (remove code duplicates)
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

    fun parse(): Expression? =
        try {
            sourceCode()
        } catch (exception: ParseException) {
            null
        }

    private fun sourceCode(): Expression {
        while (matchAndAdvanceLinesSeparator()) {
        }

        val constantsBlock = constantsBlock()

        if (constantsBlock != null && !matchAndAdvanceLinesSeparator()) {
            throw error(
                getCurrentToken(),
                "Constants block must be separated by line break"
            )
        }

        while (matchAndAdvanceLinesSeparator()) {
        }

        return SourceCode(
            constantsBlock,
            configBlockBody()
        )
    }

    private fun constantsBlock(): Expression? {
        val constantDeclarations = mutableListOf<Expression>()
        constantDeclarations.add(constantDeclaration() ?: return null)

        while (lookaheadMatchConstantDeclaration()) {
            while (matchAndAdvanceLinesSeparator()) {
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
            throw error(
                getCurrentToken(),
                "\"const\" keyword must be separated by white space character"
            )
        }

        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }

        return ConstantDeclaration(
            recordDeclaration(
                extractIdentifierBeforeWhiteSpaceChars()
            )
        )
    }

    private fun recordDeclaration(identifier: Expression): Expression {
        if (!matchAndAdvanceToken(TokenType.EQUALS)) {
            throw error(
                getCurrentToken(),
                "There must be a '=' character between identifier and value"
            )
        }

        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }

        return RecordDeclaration(identifier, recordValue())
    }

    private fun configBlock(identifier: Expression): Expression {
        if (!matchAndAdvanceToken(TokenType.LEFT_BRACE)) {
            throw error(
                getCurrentToken(),
                "There must be a '{' character at the beginning of the config block"
            )
        }

        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }

        if (!matchAndAdvanceLinesSeparator()) {
            throw error(
                getCurrentToken(),
                "Line break expected after left curly brace"
            )
        }

        while (matchAndAdvanceLinesSeparator()) {
        }

        val configBlockBody = configBlockBody()

        if (!matchAndAdvanceLinesSeparator()) {
            throw error(
                getCurrentToken(),
                "Line break expected before right curly brace"
            )
        }

        while (matchAndAdvanceLinesSeparator()) {
        }

        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }

        if (!matchAndAdvanceToken(TokenType.RIGHT_BRACE)) {
            throw error(
                getCurrentToken(),
                "There must be a '}' character at the end of the config block"
            )
        }

        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }

        return ConfigBlock(identifier, configBlockBody)
    }

    private fun configBlockBody(): Expression {
        val bodyExpressions = mutableListOf<Expression>()
        bodyExpressions.add(extractConfigBodyExpression())

        while (lookaheadMatchConfigBlockBodyIdentifier()) {
            while (matchAndAdvanceLinesSeparator()) {
            }

            bodyExpressions.add(extractConfigBodyExpression())
        }

        return ConfigBlockBody(bodyExpressions)
    }

    private fun recordValue(): Expression {
        val constantCall = constantCall()
        val escapedSequence = escapedSequence()
        val charactersSequence = charactersSequence()

        if (constantCall == null && escapedSequence == null && charactersSequence == null) {
            throw error(
                getCurrentToken(),
                "Record value can't be empty"
            )
        }

        return RecordValue(
            constantCall,
            escapedSequence,
            charactersSequence
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

        if (identifierBuilder.isBlank()) {
            throw error(
                getCurrentToken(),
                "Identifier can't be empty"
            )
        }

        return Identifier(identifierBuilder.toString())
    }

    private fun escapedSequence(): Expression? {
        if (!lookaheadMatchEscapedSequence()) {
            return null
        }

        return when {
            matchAndAdvanceToken(TokenType.BACKSLASH) -> EscapedSequence(extractEscapedText())
            else -> null
        }
    }

    private fun charactersSequence(): Expression? {
        val charactersSequenceBuilder = StringBuilder()

        while (match(*charactersSequenceMatchTokens)) {
            charactersSequenceBuilder.append(advanceToken().lexeme)
        }

        if (charactersSequenceBuilder.isBlank()) {
            return null
        }

        return CharactersSequence(charactersSequenceBuilder.toString())
    }

    private fun lookaheadMatchConstantDeclaration(): Boolean {
        val (matchMultipleLinesSeparators, shift) = lookaheadMatchMultipleLinesSeparators()

        if (!matchMultipleLinesSeparators) {
            return false
        }

        return matchConstKeyword(shift)
    }

    private fun matchConstKeyword(startShift: Int = 0): Boolean {
        return lookaheadTokenMatch(startShift, TokenType.C_LETTER)
                && lookaheadTokenMatch(shift = startShift + 1, TokenType.O_LETTER)
                && lookaheadTokenMatch(shift = startShift + 2, TokenType.N_LETTER)
                && lookaheadTokenMatch(shift = startShift + 3, TokenType.S_LETTER)
                && lookaheadTokenMatch(shift = startShift + 4, TokenType.T_LETTER)
    }

    private fun lookaheadMatchConfigBlockBodyIdentifier(): Boolean {
        val matchSeparatorsPair = lookaheadMatchMultipleLinesSeparators()

        val matchMultipleLinesSeparators = matchSeparatorsPair.first
        var shift = matchSeparatorsPair.second

        if (!matchMultipleLinesSeparators) {
            return false
        }

        while (lookaheadTokenMatch(shift, TokenType.WHITE_SPACE_CHARACTER)) {
            shift++
        }

        return matchIdentifier(shift)
    }

    private fun lookaheadMatchMultipleLinesSeparators(): Pair<Boolean, Int> {
        var (matchLinesSeparator, shift) = lookaheadMatchLinesSeparator()
        val matchResult = matchLinesSeparator

        while (matchLinesSeparator) {
            val lookaheadResult = lookaheadMatchLinesSeparator(shift)
            matchLinesSeparator = lookaheadResult.first
            shift = lookaheadResult.second
        }

        return Pair(matchResult, shift)
    }

    private fun lookaheadMatchLinesSeparator(startShift: Int = 0): Pair<Boolean, Int> {
        var shift = startShift

        while (lookaheadTokenMatch(shift, TokenType.WHITE_SPACE_CHARACTER)) {
            shift++
        }

        if (!lookaheadTokenMatch(shift, TokenType.LINE_BREAK_CHARACTER)) {
            return Pair(false, startShift)
        }

        shift++
        return Pair(true, shift)
    }

    private fun matchIdentifier(startShift: Int = 0): Boolean {
        return lookaheadTokensMatch(startShift, *identifierMatchTokens) && notSingleBraceToken(startShift)
    }

    private fun notSingleBraceToken(startShift: Int): Boolean =
        when {
            lookaheadTokenIsBrace(startShift) -> lookaheadTokensMatch(startShift + 1, *identifierMatchTokens)
            else -> true
        }

    private fun lookaheadTokenIsBrace(startShift: Int): Boolean =
        lookaheadTokenMatch(startShift, TokenType.LEFT_BRACE)
                || lookaheadTokenMatch(startShift, TokenType.RIGHT_BRACE)

    private fun lookaheadTokensMatch(shift: Int, vararg tokenTypes: TokenType): Boolean =
        tokenTypes
            .firstOrNull { tokenType -> lookaheadTokenMatch(shift, tokenType) }
            ?.let { true }
            ?: false

    private fun extractConfigBodyExpression(): Expression {
        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }

        val identifier = extractIdentifierBeforeWhiteSpaceChars()

        return when {
            currentTokenMatch(TokenType.LEFT_BRACE) -> configBlock(identifier)
            currentTokenMatch(TokenType.EQUALS) -> recordDeclaration(identifier)
            else -> throw error(getCurrentToken(), "'{' or '=' expected here")
        }
    }

    private fun extractIdentifierBeforeWhiteSpaceChars(): Expression {
        val identifier = identifier()

        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }

        return identifier
    }

    private fun matchAndAdvanceLinesSeparator(): Boolean {
        while (matchAndAdvanceToken(TokenType.WHITE_SPACE_CHARACTER)) {
        }

        return matchAndAdvanceToken(TokenType.LINE_BREAK_CHARACTER)
    }

    private fun lookaheadMatchEscapedSequence(): Boolean {
        return lookaheadTokenMatch(shift = 0, TokenType.BACKSLASH)
                && (
                lookaheadTokenMatch(shift = 1, TokenType.BACKSLASH)
                        || lookaheadTokenMatch(shift = 1, TokenType.DOLLAR_SIGN)
                )
    }

    private fun lookaheadTokenMatch(shift: Int, tokenType: TokenType) =
        isNotEndOfTokensStream()
                && (currentTokenIndex + shift < tokens.size)
                && tokens[currentTokenIndex + shift].tokenType == tokenType

    private fun extractEscapedText(): String {
        val escapedTextBuilder = StringBuilder().append(advanceToken().lexeme)

        while (matchAndAdvanceToken(TokenType.BACKSLASH)) {
            escapedTextBuilder.append(advanceToken().lexeme)
        }

        return escapedTextBuilder.toString()
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

    private fun error(token: Token, message: String): ParseException {
        reportError(token, message)
        return ParseException()
    }

}
