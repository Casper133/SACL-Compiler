package com.casper.compiler.library.expression.impl

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor

data class ConstantCall(val identifier: Identifier) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitConstantCallExpression(this)

}
