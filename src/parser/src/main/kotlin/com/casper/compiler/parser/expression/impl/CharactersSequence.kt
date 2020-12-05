package com.casper.compiler.parser.expression.impl

import com.casper.compiler.parser.expression.Expression
import com.casper.compiler.parser.visitor.Visitor

data class CharactersSequence(val text: String) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitCharactersSequenceExpression(this)

}
