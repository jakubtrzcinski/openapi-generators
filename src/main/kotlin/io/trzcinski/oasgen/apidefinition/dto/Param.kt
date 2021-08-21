package io.trzcinski.oasgen.apidefinition.dto


data class Param(
    val place: String,
    val name: ConvertableName,
    val type: ConvertableName,
    val model: Boolean
)
