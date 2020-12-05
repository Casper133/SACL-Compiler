package com.casper.compiler.parser.expression.impl

import com.casper.compiler.lexer.token.Token
import com.casper.compiler.parser.expression.Expression
import com.casper.compiler.parser.visitor.Visitor

data class EscapedSequence(val escapedCharacters: List<Token>) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitEscapedSequenceExpression(this)

}
