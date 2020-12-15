package com.casper.compiler.library.expression.impl

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor

data class ConstantsBlock(val constantDeclarations: List<ConstantDeclaration>) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitConstantsBlockExpression(this)

}
