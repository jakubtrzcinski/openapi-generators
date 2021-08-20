package io.trzcinski.oasgen.apidefinition.dto


data class CRUD(
    val name: String,
    val endpoints: List<Endpoint> = listOf(),
    val apiModels: List<ApiModel> = listOf(),
    val ownImports: List<String> = listOf(),
    val externalImports: List<ExternalImport> = listOf()
)