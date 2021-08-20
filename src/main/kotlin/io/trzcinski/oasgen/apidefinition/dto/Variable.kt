package io.trzcinski.oasgen.apidefinition.dto

data class Variable(
    val name: String,
    val type: String,
    val optional: Boolean,
    val list: Boolean,
    val model: Boolean
)
