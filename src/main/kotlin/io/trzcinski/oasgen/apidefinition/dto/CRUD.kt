package io.trzcinski.oasgen.apidefinition.dto


data class CRUD(
    val name: ConvertableName,
    val endpoints: List<Endpoint> = listOf(),
    val apiModels: List<ApiModel> = listOf(),
    val ownImports: List<ConvertableName> = listOf(),
    val externalImports: List<ExternalImport> = listOf()
)