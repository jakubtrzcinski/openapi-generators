package io.trzcinski.oasgen.apidefinition.dto

data class Endpoint(
    val path: String,
    val name: ConvertableName,
    val method: String,
    val params: List<Param>,
    val responseType: ConvertableName,
    val responseTypeIsObject: Boolean
)