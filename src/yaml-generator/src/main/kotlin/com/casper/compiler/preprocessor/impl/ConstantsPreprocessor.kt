package com.casper.compiler.preprocessor.impl

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
import com.casper.compiler.preprocessor.AstPreprocessor

class ConstantsPreprocessor : AstPreprocessor {

    private companion object {
        private const val UNDEFINED_LINE = -1
    }

    private val constants = mutableMapOf<String, String>()

    override fun runPreprocessing(ast: Expression) {
        ast.accept(this)
    }

    override fun visitSourceCodeExpression(expression: SourceCode) {
        expression.constantsBlock?.accept(this)
        expression.configBlockBody?.accept(this)
    }

    override fun visitConstantsBlockExpression(expression: ConstantsBlock) {
        expression.constantDeclarations.forEach {
            it.accept(this)
        }
    }

    override fun visitConstantDeclarationExpression(expression: ConstantDeclaration) {
        val constantIdentifier = expression.recordDeclaration.identifier.text
        constants[constantIdentifier] = expression
            .recordDeclaration
            .recordValue
            .inlineConstantCallAndEscapedSequence()
            .charactersSequence
            ?.text
            ?: throw IllegalStateException("no constant value for '$constantIdentifier'")
    }

    override fun visitConfigBlockBodyExpression(expression: ConfigBlockBody) {
        expression.bodyExpressions.forEach {
            it.accept(this)
        }
    }

    override fun visitConfigBlockExpression(expression: ConfigBlock) {
        expression.configBlockBody.accept(this)
    }

    override fun visitRecordDeclarationExpression(expression: RecordDeclaration) {
        expression.recordValue.inlineConstantCallAndEscapedSequence()
    }

    override fun visitIdentifierExpression(expression: Identifier) {
        return
    }

    override fun visitRecordValueExpression(expression: RecordValue) {
        return
    }

    override fun visitConstantCallExpression(expression: ConstantCall) {
        return
    }

    override fun visitEscapedSequenceExpression(expression: EscapedSequence) {
        return
    }

    override fun visitCharactersSequenceExpression(expression: CharactersSequence) {
        return
    }

    private fun RecordValue.inlineConstantCallAndEscapedSequence(): RecordValue {
        this.constantCall?.let {
            val constantValue =
                constants[it.identifier.text]
                    ?: throw IllegalStateException("constant '${it.identifier.text}' not defined")

            this.charactersSequence = CharactersSequence(UNDEFINED_LINE, constantValue)
            this.constantCall = null
        }

        this.escapedSequence?.let {
            val charactersSequenceText =
                this.charactersSequence?.text
                    ?: throw IllegalStateException("character sequence is empty")

            val inlinedText = "${it.text}$charactersSequenceText"
            this.charactersSequence = CharactersSequence(UNDEFINED_LINE, inlinedText)
            this.escapedSequence = null
        }

        return this
    }

}