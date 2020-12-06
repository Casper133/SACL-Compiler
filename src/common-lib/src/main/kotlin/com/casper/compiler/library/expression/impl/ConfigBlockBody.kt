package com.casper.compiler.library.expression.impl

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor

data class ConfigBlockBody(val bodyExpressions: List<Expression>) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitConfigBlockBodyExpression(this)

}
