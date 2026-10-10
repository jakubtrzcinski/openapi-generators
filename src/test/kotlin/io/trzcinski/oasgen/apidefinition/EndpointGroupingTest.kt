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

class EndpointGroupingTest {
    @Test
    fun `explicit operation group preserves unrelated DTO origins and URL`() {
        val mapper = DtoMapper()
        val result = SwaggerApiDefinitionCreator(
            OASSupplierFactory(), EndpointAggregator(PathMapper(mapper), mapper),
            OpenAPIParser(), CRUDAggregator()
        ).execute(javaClass.getResource("/endpoint-grouping.yaml")!!.path)

        assertFalse(result.cruds.any { it.name.pascalCase == "Me" })
        val region = result.cruds.single { it.name.pascalCase == "Region" }
        assertEquals("/me/region", region.endpoints.single().path)
        assertEquals("UserRegion", region.apiModels.single().name.pascalCase)
        val commons = result.cruds.single { it.name.pascalCase == "Commons" }
        assertEquals("/status", commons.endpoints.single().path)
        assertEquals(setOf("Measurement", "WeightMeasurement", "MeasurementDietSnapshot",
            "DietProductNutrientMetadata", "SocialMergeRequest"),
            commons.apiModels.map { it.name.pascalCase }.toSet())
        assertEquals("/items", result.cruds.single { it.name.pascalCase == "Items" }.endpoints.single().path)
    }
}
