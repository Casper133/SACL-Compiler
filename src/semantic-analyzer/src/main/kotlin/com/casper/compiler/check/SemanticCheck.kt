package com.casper.compiler.check

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor

interface SemanticCheck : Visitor<Unit> {

    fun checkAst(ast: Expression)

}
