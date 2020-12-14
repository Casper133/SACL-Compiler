package com.casper.compiler.check.impl

import com.casper.compiler.check.SemanticCheck
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

class DuplicateIdentifiersOnSameLevelCheck : SemanticCheck {

    override fun checkAst(ast: Expression) {
        return
    }

    override fun visitSourceCodeExpression(expression: SourceCode) {
        TODO("Not yet implemented")
    }

    override fun visitConstantsBlockExpression(expression: ConstantsBlock) {
        TODO("Not yet implemented")
    }

    override fun visitConstantDeclarationExpression(expression: ConstantDeclaration) {
        TODO("Not yet implemented")
    }

    override fun visitConfigBlockBodyExpression(expression: ConfigBlockBody) {
        TODO("Not yet implemented")
    }

    override fun visitConfigBlockExpression(expression: ConfigBlock) {
        TODO("Not yet implemented")
    }

    override fun visitRecordDeclarationExpression(expression: RecordDeclaration) {
        TODO("Not yet implemented")
    }

    override fun visitIdentifierExpression(expression: Identifier) {
        TODO("Not yet implemented")
    }

    override fun visitRecordValueExpression(expression: RecordValue) {
        TODO("Not yet implemented")
    }

    override fun visitConstantCallExpression(expression: ConstantCall) {
        TODO("Not yet implemented")
    }

    override fun visitEscapedSequenceExpression(expression: EscapedSequence) {
        TODO("Not yet implemented")
    }

    override fun visitCharactersSequenceExpression(expression: CharactersSequence) {
        TODO("Not yet implemented")
    }
}