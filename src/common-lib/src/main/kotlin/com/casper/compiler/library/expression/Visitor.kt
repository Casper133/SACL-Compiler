package com.casper.compiler.library.expression

import com.casper.compiler.library.expression.impl.CharactersSequence
import com.casper.compiler.library.expression.impl.ConfigBlock
import com.casper.compiler.library.expression.impl.ConfigBlockBody
import com.casper.compiler.library.expression.impl.ConstantCall
import com.casper.compiler.library.expression.impl.ConstantDeclaration
import com.casper.compiler.library.expression.impl.ConstantsBlock
import com.casper.compiler.library.expression.impl.EscapedSequence
import com.casper.compiler.library.expression.impl.Identifier
import com.casper.compiler.library.expression.impl.NameValuePair
import com.casper.compiler.library.expression.impl.RecordDeclaration
import com.casper.compiler.library.expression.impl.RecordValue
import com.casper.compiler.library.expression.impl.SourceCode

interface Visitor<R> {

    fun visitSourceCodeExpression(expression: SourceCode): R
    fun visitConstantsBlockExpression(expression: ConstantsBlock): R
    fun visitConstantDeclarationExpression(expression: ConstantDeclaration): R
    fun visitConfigBlockBodyExpression(expression: ConfigBlockBody): R
    fun visitConfigBlockExpression(expression: ConfigBlock): R
    fun visitNameValuePairExpression(expression: NameValuePair): R
    fun visitRecordDeclarationExpression(expression: RecordDeclaration): R
    fun visitIdentifierExpression(expression: Identifier): R
    fun visitRecordValueExpression(expression: RecordValue): R
    fun visitConstantCallExpression(expression: ConstantCall): R
    fun visitEscapedSequenceExpression(expression: EscapedSequence): R
    fun visitCharactersSequenceExpression(expression: CharactersSequence): R

}
