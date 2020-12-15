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

class ConstantsUsageCheck : SemanticCheck {

    private val constantIdentifiers = mutableListOf<String>()

    override fun checkAst(ast: Expression) {
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
        constantIdentifiers.add(expression.recordDeclaration.identifier.text)
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
        expression.recordValue.accept(this)
    }

    override fun visitIdentifierExpression(expression: Identifier) {
        return
    }

    override fun visitRecordValueExpression(expression: RecordValue) {
        expression.constantCall?.accept(this)
    }

    override fun visitConstantCallExpression(expression: ConstantCall) {
        val constantIdentifier = expression.identifier.text

        if (constantIdentifiers.contains(constantIdentifier)) return

        reportError(
            expression.identifier.line,
            "constant identifier '$constantIdentifier' not defined"
        )
    }

    override fun visitEscapedSequenceExpression(expression: EscapedSequence) {
        return
    }

    override fun visitCharactersSequenceExpression(expression: CharactersSequence) {
        return
    }
}
