package io.trzcinski.oasgen.apidefinition

import io.swagger.parser.OpenAPIParser
import io.swagger.v3.parser.core.models.ParseOptions
import io.trzcinski.oasgen.apidefinition.dto.CRUDAggregate
import io.trzcinski.oasgen.apidefinition.swagger.CRUDAggregator
import io.trzcinski.oasgen.apidefinition.swagger.EndpointAggregator
import io.trzcinski.oasgen.oas.supplier.OASSupplierFactory

class SwaggerApiDefinitionCreator(
    private val supplierFactory: OASSupplierFactory,
    private val mapper: EndpointAggregator,
    private val parser: OpenAPIParser,
    private val definitionFactory: CRUDAggregator,
) {
    fun execute(source: String): CRUDAggregate {
        val options = ParseOptions().apply { isResolve = true }
        return supplierFactory.fromRawSource(source).get()
            .let { parser.readContents(it, null, options).openAPI }
            .let(mapper::run)
            .let(definitionFactory::run)
    }
}
