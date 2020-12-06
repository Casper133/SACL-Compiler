package com.casper.compiler.library.expression

interface Expression {

    fun <R> accept(visitor: Visitor<R>): R

}
