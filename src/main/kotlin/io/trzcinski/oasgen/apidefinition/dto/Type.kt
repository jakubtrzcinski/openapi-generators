package io.trzcinski.oasgen.apidefinition.dto

data class Type(
    val name: ConvertableName,
    val optional: Boolean,
    val list: Boolean
)