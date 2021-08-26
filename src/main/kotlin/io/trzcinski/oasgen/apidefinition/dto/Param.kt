package io.trzcinski.oasgen.apidefinition.dto


data class Param(
    val place: String,
    val name: ConvertableName,
    val alias: String,
    val type: Type
)
