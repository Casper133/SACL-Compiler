package com.casper.compiler

import com.casper.compiler.charset.resolveCharset
import com.casper.compiler.lexer.Lexer
import com.casper.compiler.library.hadError
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
    Lexer(String(sourceCode, charset))
        .scanTokens()
        .forEach(::println)

    if (hadError) exitProcess(1)
}
