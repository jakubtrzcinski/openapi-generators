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
    val externalCrudImports: MutableList<ExternalImport>,
    val oneOfVariants: List<OneOfVariant> = emptyList(),
    val oneOfParents: List<ConvertableName> = emptyList(),
    val oneOfDiscriminator: String? = null
) {
    constructor(
        origin: ConvertableName,
        name: ConvertableName,
        properties: List<Variable>,
        ownCrudImports: MutableList<ConvertableName>,
        externalCrudImports: MutableList<ExternalImport>
    ) : this(origin, name, properties, ownCrudImports, externalCrudImports, emptyList(), emptyList(), null)
}
