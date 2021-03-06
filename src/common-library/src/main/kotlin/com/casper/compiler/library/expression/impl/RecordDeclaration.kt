package com.casper.compiler.library.expression.impl

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor

data class RecordDeclaration(
    val identifier: Identifier,
    val recordValue: RecordValue,
) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitRecordDeclarationExpression(this)

}
