package io.trzcinski.oasgen.apidefinition.dto

data class Endpoint(
    val path: String,
    val name: String,
    val method: String,
    val params: List<Param>,
    val responseType: String,
    val responseTypeIsObject: Boolean
)