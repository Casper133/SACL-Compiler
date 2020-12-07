package com.casper.compiler.library.expression.impl

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor

data class EscapedSequence(val text: String) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitEscapedSequenceExpression(this)

}
