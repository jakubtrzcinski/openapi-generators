package io.trzcinski.oasgen.apidefinition.dto

data class Variable(
    val name: ConvertableName,
    val type: ConvertableName,
    val optional: Boolean,
    val list: Boolean,
    val model: Boolean
)
