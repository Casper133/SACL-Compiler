package com.casper.compiler

import com.casper.compiler.charset.resolveCharset
import com.casper.compiler.library.error.hadError
import java.io.File
import kotlin.system.exitProcess

private const val YAML_FILE_EXTENSION = "yaml"

fun main(args: Array<String>) {
    if (args.size != 1) {
        System.err.println("First argument must be the name of the file with source code.")
        exitProcess(1)
    }

    compileCode(args[0])
}

private fun compileCode(path: String) {
    val sourceCodeFile = File(path)

    if (!sourceCodeFile.exists()) {
        System.err.println("File '${sourceCodeFile.name}' not exists.")
        exitProcess(1)
    }

    val sourceCode = sourceCodeFile.readBytes()
    val charset = resolveCharset(sourceCode)

    val tokens = Lexer(String(sourceCode, charset)).scanTokens()
    val ast = Parser(tokens).parse()

    ast ?: return
    exitProcessIfErrorOccurs()

    SemanticAnalyzer(ast).runChecks()
    exitProcessIfErrorOccurs()

    val generatedFileName = "${sourceCodeFile.nameWithoutExtension}.$YAML_FILE_EXTENSION"
    val generatedFile = File(generatedFileName).also(File::createNewFile)

    YamlGenerator(ast, generatedFile).generateYAML()
    println("File '$generatedFileName' compiled successfully.")
}

private fun exitProcessIfErrorOccurs() {
    if (hadError) exitProcess(1)
}
