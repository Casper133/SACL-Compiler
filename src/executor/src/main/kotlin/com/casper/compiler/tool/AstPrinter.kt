package com.casper.compiler.tool

import com.casper.compiler.library.token.Token
import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.impl.CharactersSequence
import com.casper.compiler.library.expression.impl.ConfigBlock
import com.casper.compiler.library.expression.impl.ConfigBlockBody
import com.casper.compiler.library.expression.impl.ConstantCall
import com.casper.compiler.library.expression.impl.ConstantDeclaration
import com.casper.compiler.library.expression.impl.ConstantsBlock
import com.casper.compiler.library.expression.impl.EscapedSequence
import com.casper.compiler.library.expression.impl.Identifier
import com.casper.compiler.library.expression.impl.NameValuePair
import com.casper.compiler.library.expression.impl.RecordDeclaration
import com.casper.compiler.library.expression.impl.RecordValue
import com.casper.compiler.library.expression.impl.SourceCode
import com.casper.compiler.library.expression.Visitor

class AstPrinter : Visitor<String> {

    fun print(expression: Expression): String = expression.accept(this)

    override fun visitSourceCodeExpression(expression: SourceCode): String =
        parenthesize(
            "SourceCode",
            expression.constantsBlock,
            expression.configBlockBody
        )

    override fun visitConstantsBlockExpression(expression: ConstantsBlock): String =
        parenthesize(
            "ConstantsBlock",
            *expression.constantDeclarations.toTypedArray()
        )

    override fun visitConstantDeclarationExpression(expression: ConstantDeclaration): String =
        parenthesize(
            "ConstantDeclaration",
            expression.recordDeclaration
        )

    override fun visitConfigBlockBodyExpression(expression: ConfigBlockBody): String =
        parenthesize(
            "ConfigBlockBody",
            *expression.bodyExpressions.toTypedArray()
        )

    override fun visitConfigBlockExpression(expression: ConfigBlock): String =
        parenthesize(
            "ConfigBlock",
            expression.identifier,
            expression.configBlockBody
        )

    override fun visitNameValuePairExpression(expression: NameValuePair): String =
        parenthesize(
            "NameValuePair",
            expression.recordDeclaration
        )

    override fun visitRecordDeclarationExpression(expression: RecordDeclaration): String =
        parenthesize(
            "RecordDeclaration",
            expression.identifier,
            expression.recordValue
        )

    override fun visitIdentifierExpression(expression: Identifier): String =
        "(Identifier ${expression.text})"

    override fun visitRecordValueExpression(expression: RecordValue): String =
        parenthesize(
            "RecordValue",
            expression.constantCall,
            expression.escapedSequence,
            expression.charactersSequence
        )

    override fun visitConstantCallExpression(expression: ConstantCall): String =
        parenthesize(
            "ConstantCall",
            expression.identifier
        )

    override fun visitEscapedSequenceExpression(expression: EscapedSequence): String =
        "(EscapedSequence ${
            expression
                .escapedCharacters
                .joinToString(separator = " ", transform = Token::lexeme)
        })"

    override fun visitCharactersSequenceExpression(expression: CharactersSequence): String =
        "(CharactersSequence ${expression.text})"

    private fun parenthesize(name: String, vararg expressions: Expression?): String =
        StringBuilder()
            .append("(")
            .append(name)
            .also { builder ->
                expressions.forEach { expression ->
                    builder
                        .append(
                            expression
                                ?.also { builder.append(" ") }
                                ?.accept(this)
                                ?: ""
                        )
                }
            }
            .append(")")
            .toString()
}
