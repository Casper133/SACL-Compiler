package com.casper.compiler.library.expression.impl

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor

data class NameValuePair(val recordDeclaration: Expression) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitNameValuePairExpression(this)

}