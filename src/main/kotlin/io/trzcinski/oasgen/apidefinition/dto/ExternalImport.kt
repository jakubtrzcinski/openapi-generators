package io.trzcinski.oasgen.apidefinition.dto


data class ExternalImport(
    val crud: ConvertableName,
    val name: ConvertableName
)