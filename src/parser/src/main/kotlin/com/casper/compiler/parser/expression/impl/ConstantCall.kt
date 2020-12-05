package com.casper.compiler.parser.expression.impl

import com.casper.compiler.parser.expression.Expression
import com.casper.compiler.parser.visitor.Visitor

data class ConstantCall(val identifier: Expression) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitConstantCallExpression(this)

}
