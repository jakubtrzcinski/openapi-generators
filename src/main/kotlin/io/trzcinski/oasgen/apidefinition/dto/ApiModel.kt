package io.trzcinski.oasgen.apidefinition.dto


/**
 * An Representation for definition from OpenApiSpecification
 */
data class ApiModel(
    val name: String,
    val properties: List<Variable>,
    /**
     * Names of the models that are used in current model and have same origin
     */
    val ownCrudImports: List<String>,
    val externalCrudImports: List<ExternalImport>
)