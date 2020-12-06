package com.casper.compiler

import com.casper.compiler.charset.resolveCharset
import com.casper.compiler.library.error.hadError
import com.casper.compiler.library.expression.impl.CharactersSequence
import com.casper.compiler.library.expression.impl.ConstantDeclaration
import com.casper.compiler.library.expression.impl.ConstantsBlock
import com.casper.compiler.library.expression.impl.Identifier
import com.casper.compiler.library.expression.impl.RecordDeclaration
import com.casper.compiler.library.expression.impl.RecordValue
import com.casper.compiler.library.expression.impl.SourceCode
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
    Lexer(String(sourceCode, charset))
        .scanTokens()
        .forEach(::println)

    val expression =
        SourceCode(
            constantsBlock = ConstantsBlock(
                listOf(
                    ConstantDeclaration(
                        RecordDeclaration(
                            Identifier("TRUE_CONSTANT"),
                            RecordValue(charactersSequence = CharactersSequence("true"))
                        )
                    ),
                    ConstantDeclaration(
                        RecordDeclaration(
                            Identifier("FILE_SIZE"),
                            RecordValue(charactersSequence = CharactersSequence("10MB"))
                        )
                    ),
                    ConstantDeclaration(
                        RecordDeclaration(
                            Identifier("TRACE_LOGGING_LEVEL"),
                            RecordValue(charactersSequence = CharactersSequence("TRACE"))
                        )
                    )
                )
            )
        )

    println("\nExpression:\n${AstPrinter().print(expression)}")

    if (hadError) exitProcess(1)
}
