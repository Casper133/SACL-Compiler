package com.casper.compiler.library.expression.impl

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor

data class Identifier(val text: String) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitIdentifierExpression(this)

}
