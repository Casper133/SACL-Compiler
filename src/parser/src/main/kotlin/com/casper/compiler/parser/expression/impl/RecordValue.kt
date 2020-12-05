package com.casper.compiler.parser.expression.impl

import com.casper.compiler.parser.expression.Expression
import com.casper.compiler.parser.visitor.Visitor

data class RecordValue(
    val constantCall: Expression? = null,
    val escapedSequence: Expression? = null,
    val charactersSequence: Expression? = null
) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitRecordValueExpression(this)

}
