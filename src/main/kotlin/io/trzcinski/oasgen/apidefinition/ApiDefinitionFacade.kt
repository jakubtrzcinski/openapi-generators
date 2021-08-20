package io.trzcinski.oasgen.apidefinition

import io.trzcinski.oasgen.apidefinition.dto.CRUDAggregate

class ApiDefinitionFacade(
    private val swagger: SwaggerApiDefinitionCreator
) {
    fun readFromSwaggerRawSource(source: String): CRUDAggregate {
        return swagger.execute(source)
    }
}