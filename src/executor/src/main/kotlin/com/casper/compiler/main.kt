package com.casper.compiler

import com.casper.compiler.charset.resolveCharset
import com.casper.compiler.library.error.hadError
import com.casper.compiler.tool.AstPrinter
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 1) {
        System.err.println("First argument must be the name of the file with source code.")
        exitProcess(1)
    }

    compileCode(args[0])
}

fun compileCode(path: String) {
    val sourceCode = File(path).readBytes()
    val charset = resolveCharset(sourceCode)
    val tokens = Lexer(String(sourceCode, charset)).scanTokens()
    val ast = Parser(tokens).parse()

    if (hadError) return

    println("\nAST:\n${ast?.let(AstPrinter()::print)}")
}
