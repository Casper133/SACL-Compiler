package com.casper.compiler.parser.visitor

import com.casper.compiler.parser.expression.impl.CharactersSequence
import com.casper.compiler.parser.expression.impl.ConfigBlock
import com.casper.compiler.parser.expression.impl.ConfigBlockBody
import com.casper.compiler.parser.expression.impl.ConstantCall
import com.casper.compiler.parser.expression.impl.ConstantDeclaration
import com.casper.compiler.parser.expression.impl.ConstantsBlock
import com.casper.compiler.parser.expression.impl.EscapedSequence
import com.casper.compiler.parser.expression.impl.Identifier
import com.casper.compiler.parser.expression.impl.NameValuePair
import com.casper.compiler.parser.expression.impl.RecordDeclaration
import com.casper.compiler.parser.expression.impl.RecordValue
import com.casper.compiler.parser.expression.impl.SourceCode

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
