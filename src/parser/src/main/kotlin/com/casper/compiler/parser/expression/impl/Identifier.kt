package com.casper.compiler.parser.expression.impl

import com.casper.compiler.parser.expression.Expression
import com.casper.compiler.parser.visitor.Visitor

data class Identifier(val text: String) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitIdentifierExpression(this)

}
