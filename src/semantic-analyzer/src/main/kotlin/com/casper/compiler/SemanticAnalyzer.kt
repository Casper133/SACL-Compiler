package com.casper.compiler

import com.casper.compiler.check.SemanticCheck
import com.casper.compiler.check.impl.ConstantsUsageCheck
import com.casper.compiler.check.impl.DuplicateIdentifiersOnSameLevelCheck
import com.casper.compiler.library.error.hadError
import com.casper.compiler.library.expression.Expression

class SemanticAnalyzer(private val ast: Expression) {

    private val semanticChecks: List<SemanticCheck> = listOf(
        ConstantsUsageCheck(),
        DuplicateIdentifiersOnSameLevelCheck()
    )

    fun runChecks() {
        semanticChecks.forEach {
            it.checkAst(ast)
            if (hadError) return@forEach
        }
    }

}
