package com.casper.compiler.preprocessor

import com.casper.compiler.library.expression.Expression
import com.casper.compiler.library.expression.Visitor

interface AstPreprocessor : Visitor<Unit> {

    fun runPreprocessing(ast: Expression)

}
