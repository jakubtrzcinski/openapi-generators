package io.trzcinski.oasgen.apidefinition

import io.swagger.parser.OpenAPIParser
import io.trzcinski.oasgen.apidefinition.swagger.CRUDAggregator
import io.trzcinski.oasgen.apidefinition.swagger.EndpointAggregator
import io.trzcinski.oasgen.apidefinition.swagger.mapper.DtoMapper
import io.trzcinski.oasgen.apidefinition.swagger.mapper.PathMapper
import io.trzcinski.oasgen.oas.supplier.OASSupplierFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ContractFidelityTest {
    private fun generate() = DtoMapper().let { dtoMapper ->
        SwaggerApiDefinitionCreator(
            OASSupplierFactory(), EndpointAggregator(PathMapper(dtoMapper), dtoMapper),
            OpenAPIParser(), CRUDAggregator()
        ).execute(javaClass.getResource("/contract-fidelity.yaml")!!.path)
    }

    @Test
    fun `whole generator registers oneOf and enum metadata`() {
        val models = generate().cruds.flatMap { it.apiModels }
        assertFalse(models.any { it.name.value == "Kind" })
        assertEquals(listOf("One", "Two"), models.single { it.name.value == "Union" }.oneOfVariants.map { it.type.value })
        assertEquals("String", models.single { it.name.value == "One" }.properties.single().type.name.value)
    }

    @Test
    fun `whole generator retains inherited resolved parameters and created response`() {
        val endpoint = generate().cruds.flatMap { it.endpoints }.single()
        assertEquals(mapOf("id" to false, "query" to true), endpoint.params.associate { it.name.camelCase to it.type.optional })
        assertEquals("Union", endpoint.responseType.name.value)
    }
}
