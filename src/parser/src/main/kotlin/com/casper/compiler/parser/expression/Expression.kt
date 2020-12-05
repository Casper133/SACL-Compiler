package com.casper.compiler.parser.expression

import com.casper.compiler.parser.visitor.Visitor

interface Expression {

    fun <R> accept(visitor: Visitor<R>): R

}
