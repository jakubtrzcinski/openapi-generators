package io.trzcinski.oasgen.apidefinition.dto


/**
 * An Representation for definition from OpenApiSpecification
 */
data class ApiModel(
    var origin: ConvertableName,
    val name: ConvertableName,
    val properties: List<Variable>,
    /**
     * Names of the models that are used in current model and have same origin
     */
    val ownCrudImports: MutableList<ConvertableName>,
    val externalCrudImports: MutableList<ExternalImport>
)