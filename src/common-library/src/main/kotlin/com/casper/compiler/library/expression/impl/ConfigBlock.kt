package com.casper.compiler.library.expression.impl

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor

data class ConfigBlock(
    val identifier: Expression,
    val configBlockBody: Expression,
) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitConfigBlockExpression(this)

}
