package io.trzcinski.oasgen.apidefinition.dto

data class Endpoint(
    val basePath: String,
    val path: String,
    val name: ConvertableName,
    val method: String,
    val params: List<Param>,
    val responseType: Type
)