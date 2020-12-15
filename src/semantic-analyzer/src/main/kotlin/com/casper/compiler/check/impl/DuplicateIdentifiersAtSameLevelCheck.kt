package com.casper.compiler.check.impl

import com.casper.compiler.check.SemanticCheck
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

class DuplicateIdentifiersAtSameLevelCheck : SemanticCheck {

    override fun checkAst(ast: Expression) {
        ast.accept(this)
    }

    override fun visitSourceCodeExpression(expression: SourceCode) {
        expression.configBlockBody?.accept(this)
    }

    override fun visitConstantsBlockExpression(expression: ConstantsBlock) {
        return
    }

    override fun visitConstantDeclarationExpression(expression: ConstantDeclaration) {
        return
    }

    override fun visitConfigBlockBodyExpression(expression: ConfigBlockBody) {
        val currentLevelIdentifiers = mutableListOf<Identifier>()

        expression.bodyExpressions.forEach { bodyExpression ->
            when (bodyExpression) {
                is ConfigBlock -> currentLevelIdentifiers.add(bodyExpression.identifier)
                is RecordDeclaration -> currentLevelIdentifiers.add(bodyExpression.identifier)
                else -> return@forEach
            }
        }

        val duplicateIdentifiers = currentLevelIdentifiers
            .groupingBy { it.text }
            .eachCount()
            .filter { it.value > 1 }

        duplicateIdentifiers.forEach { (identifierText, _) ->
            val identifierLines = currentLevelIdentifiers
                .filter { it.text == identifierText }
                .map { it.line }
                .toList()

            reportError(
                identifierLines,
                "duplicate identifiers '${identifierText}' at the same config level"
            )
        }

        expression.bodyExpressions.forEach {
            it.accept(this)
        }
    }

    override fun visitConfigBlockExpression(expression: ConfigBlock) {
        expression.configBlockBody.accept(this)
    }

    override fun visitRecordDeclarationExpression(expression: RecordDeclaration) {
        return
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
}