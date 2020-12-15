package com.casper.compiler.library.expression.impl

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor

data class SourceCode(
    val constantsBlock: ConstantsBlock? = null,
    val configBlockBody: ConfigBlockBody? = null,
) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitSourceCodeExpression(this)

}
