package io.trzcinski.oasgen.apidefinition.dto

data class OneOfVariant(
    val discriminatorValue: String,
    val type: ConvertableName
)
