package com.casper.compiler.library.expression.impl

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor

data class RecordValue(
    var constantCall: ConstantCall? = null,
    var escapedSequence: EscapedSequence? = null,
    var charactersSequence: CharactersSequence? = null,
) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitRecordValueExpression(this)

}
