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
    fun `required nullable property preserves presence separately from nullability`() {
        val mapper = DtoMapper()
        val schema = Schema<Any>().type("string").nullable(true)
        val type = mapper.getType(schema, false)
        assertEquals(false, type.optional)
        assertEquals(true, type.nullable)
    }

    @Test
    fun `typed additional properties preserve referenced map values`() {
        val mapper = DtoMapper()
        val schema = Schema<Any>().type("object").additionalProperties(
            Schema<Any>().`$ref`("#/components/schemas/Child")
        )
        val type = mapper.getType(schema, false)
        assertEquals("Child", type.mapValue?.name?.value)
    }

    @Test
    fun `map aliases preserve nested nullable values and optional presence`() {
        val mapper = DtoMapper()
        val alias = Schema<Any>().type("object").additionalProperties(
            Schema<Any>().type("string").nullable(true)
        )
        mapper.registerEnumSchemas(mapOf("Labels" to alias))
        val type = mapper.getType(Schema<Any>().`$ref`("#/components/schemas/Labels"), true)
        assertEquals(true, type.optional)
        assertEquals(true, type.nullable)
        assertEquals("String", type.mapValue?.name?.value)
        assertEquals(true, type.mapValue?.nullable)
    }

    @Test
    fun `optional plain maps and model lists retain existing shapes`() {
        val mapper = DtoMapper()
        val plain = mapper.getType(Schema<Any>().type("object").additionalProperties(true), true)
        assertEquals("Object", plain.name.value)
        assertEquals(null, plain.mapValue)
        assertEquals(true, plain.nullable)
        val list = mapper.getType(io.swagger.v3.oas.models.media.ArraySchema().items(
            Schema<Any>().`$ref`("#/components/schemas/Child")
        ) as Schema<Any>, false)
        assertEquals("Child", list.name.value)
        assertEquals(true, list.list)
        assertEquals(false, list.optional)
        assertEquals(false, list.nullable)
    }

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
