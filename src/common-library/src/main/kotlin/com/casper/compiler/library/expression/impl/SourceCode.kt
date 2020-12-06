package com.casper.compiler.library.expression.impl

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor

data class SourceCode(
    val constantsBlock: Expression? = null,
    val configBlockBody: Expression? = null
) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitSourceCodeExpression(this)

}
