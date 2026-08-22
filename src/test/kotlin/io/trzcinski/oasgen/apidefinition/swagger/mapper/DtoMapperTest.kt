package io.trzcinski.oasgen.apidefinition.swagger.mapper

import io.swagger.v3.oas.models.media.ComposedSchema
import io.swagger.v3.oas.models.media.Discriminator
import io.swagger.v3.oas.models.media.ObjectSchema
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.media.StringSchema
import kotlin.test.Test
import kotlin.test.assertEquals

class DtoMapperTest {
    @Test
    fun `maps discriminated oneOf schemas to typed union metadata`() {
        val measurementKind = StringSchema().apply {
            enum = listOf("WEIGHT", "NOTE")
        }
        val createWeight = ObjectSchema().apply {
            required = listOf("kind")
            properties = linkedMapOf<String, Schema<Any>>(
                "kind" to (ComposedSchema().addAllOfItem(
                    Schema<Any>().`$ref`("#/components/schemas/MeasurementKind")
                ) as Schema<Any>)
            )
        }
        val createNote = ObjectSchema().apply {
            required = listOf("kind")
            properties = linkedMapOf<String, Schema<Any>>(
                "kind" to (ComposedSchema().addAllOfItem(
                    Schema<Any>().`$ref`("#/components/schemas/MeasurementKind")
                ) as Schema<Any>)
            )
        }
        val createMeasurement = ComposedSchema().apply {
            properties = linkedMapOf(
                "kind" to Schema<Any>().`$ref`("#/components/schemas/MeasurementKind")
            )
            addOneOfItem(Schema<Any>().`$ref`("#/components/schemas/CreateWeightMeasurement"))
            addOneOfItem(Schema<Any>().`$ref`("#/components/schemas/CreateNoteMeasurement"))
            discriminator = Discriminator()
                .propertyName("kind")
                .mapping("WEIGHT", "#/components/schemas/CreateWeightMeasurement")
                .mapping("NOTE", "#/components/schemas/CreateNoteMeasurement")
        }
        val schemas = linkedMapOf<String, Schema<Any>>(
            "MeasurementKind" to measurementKind as Schema<Any>,
            "CreateMeasurement" to createMeasurement,
            "CreateWeightMeasurement" to createWeight as Schema<Any>,
            "CreateNoteMeasurement" to createNote as Schema<Any>
        )
        val mapper = DtoMapper()

        mapper.registerEnumSchemas(schemas)

        val union = mapper.map(schemas.entries.first { it.key == "CreateMeasurement" })
        val weight = mapper.map(schemas.entries.first { it.key == "CreateWeightMeasurement" })

        assertEquals("kind", union.oneOfDiscriminator)
        assertEquals(
            listOf("WEIGHT" to "CreateWeightMeasurement", "NOTE" to "CreateNoteMeasurement"),
            union.oneOfVariants.map { it.discriminatorValue to it.type.value }
        )
        assertEquals(listOf("CreateMeasurement"), weight.oneOfParents.map { it.value })
        assertEquals("String", weight.properties.single { it.name.camelCase == "kind" }.type.name.value)
    }
}
