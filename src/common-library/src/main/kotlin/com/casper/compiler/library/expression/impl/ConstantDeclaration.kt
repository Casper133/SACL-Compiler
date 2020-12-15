package com.casper.compiler.library.expression.impl

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor

data class ConstantDeclaration(val recordDeclaration: RecordDeclaration) : Expression {

    override fun <R> accept(visitor: Visitor<R>): R = visitor.visitConstantDeclarationExpression(this)

}
