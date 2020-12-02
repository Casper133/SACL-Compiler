package com.casper.compiler.charset

import java.nio.charset.Charset

private const val UTF_8_BOM_LENGTH = 3
private const val UTF_16_BOM_LENGTH = 2

private const val UTF_8_BOM_1 = 0xEF
private const val UTF_8_BOM_2 = 0xBB
private const val UTF_8_BOM_3 = 0xBF

private const val UTF_16_BE_BOM_1 = 0xFE
private const val UTF_16_BE_BOM_2 = 0xFF

private const val UTF_16_LE_BOM_1 = 0xFF
private const val UTF_16_LE_BOM_2 = 0xFE

fun resolveCharset(sourceCodeBytes: ByteArray): Charset {
    return when {
        sourceCodeBytes.isUTF8() -> Charsets.UTF_8
        sourceCodeBytes.isUTF16BE() -> Charsets.UTF_16BE
        sourceCodeBytes.isUTF16LE() -> Charsets.UTF_16LE
        else -> Charsets.UTF_8
    }
}

private fun ByteArray.isUTF8(): Boolean {
    return this.size >= UTF_8_BOM_LENGTH
            && this[0].toUnsignedInt() == UTF_8_BOM_1
            && this[1].toUnsignedInt() == UTF_8_BOM_2
            && this[2].toUnsignedInt() == UTF_8_BOM_3
}

private fun ByteArray.isUTF16BE(): Boolean {
    return this.size >= UTF_16_BOM_LENGTH
            && this[0].toUnsignedInt() == UTF_16_BE_BOM_1
            && this[1].toUnsignedInt() == UTF_16_BE_BOM_2
}

private fun ByteArray.isUTF16LE(): Boolean {
    return this.size >= UTF_16_BOM_LENGTH
            && this[0].toUnsignedInt() == UTF_16_LE_BOM_1
            && this[1].toUnsignedInt() == UTF_16_LE_BOM_2
}

private fun Byte.toUnsignedInt(): Int {
    return toInt() and 0xFF
}
