package com.casper.compiler.library.expression.impl

import com.casper.compiler.library.token.Token
import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor

data class EscapedSequence(val escapedCharacters: List<Token>) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitEscapedSequenceExpression(this)

}
