package com.casper.compiler.parser.expression.impl

import com.casper.compiler.parser.expression.Expression
import com.casper.compiler.parser.visitor.Visitor

data class SourceCode(
    val constantsBlock: Expression? = null,
    val configBlockBody: Expression? = null
) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitSourceCodeExpression(this)

}
