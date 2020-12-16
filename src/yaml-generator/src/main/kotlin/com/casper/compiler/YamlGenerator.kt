package com.casper.compiler

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor
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
import com.casper.compiler.preprocessor.impl.ConstantsPreprocessor
import java.io.File

class YamlGenerator(
    private val ast: Expression,
    private val generatedFile: File,
) : Visitor<Unit> {

    private companion object {
        private const val DEFAULT_INDENT = "  "
    }

    private var currentNestingLevel = 0
    private var resultCodeBuilder = StringBuilder()

    fun generateYAML() {
        ConstantsPreprocessor().runPreprocessing(ast)
        ast.accept(this)
        generatedFile.writeText(resultCodeBuilder.toString(), Charsets.UTF_8)
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
        expression.bodyExpressions.forEach {
            it.accept(this)
        }
    }

    override fun visitConfigBlockExpression(expression: ConfigBlock) {
        currentNestingLevel++

        resultCodeBuilder
            .append(expression.identifier.text)
            .append(":")
            .append("\n")
            .append(DEFAULT_INDENT.repeat(currentNestingLevel))

        expression.configBlockBody.accept(this)

        currentNestingLevel--

        val lastIndentIndex = resultCodeBuilder.lastIndexOf(DEFAULT_INDENT)

        resultCodeBuilder = resultCodeBuilder
            .deleteRange(
                lastIndentIndex, resultCodeBuilder.length
            )
    }

    override fun visitRecordDeclarationExpression(expression: RecordDeclaration) {
        resultCodeBuilder
            .append(expression.identifier.text)
            .append(": ")
            .append(expression.recordValue.charactersSequence?.text ?: "")
            .append("\n")
            .append(DEFAULT_INDENT.repeat(currentNestingLevel))

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
