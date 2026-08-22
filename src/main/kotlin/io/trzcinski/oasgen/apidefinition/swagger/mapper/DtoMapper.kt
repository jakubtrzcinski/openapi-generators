package io.trzcinski.oasgen.apidefinition.swagger.mapper

import io.swagger.v3.oas.models.media.ArraySchema
import io.swagger.v3.oas.models.media.ComposedSchema
import io.swagger.v3.oas.models.media.DateSchema
import io.swagger.v3.oas.models.media.DateTimeSchema
import io.swagger.v3.oas.models.media.Schema
import io.trzcinski.oasgen.apidefinition.dto.ApiModel
import io.trzcinski.oasgen.apidefinition.dto.ConvertableName
import io.trzcinski.oasgen.apidefinition.dto.OneOfVariant
import io.trzcinski.oasgen.apidefinition.dto.Type
import io.trzcinski.oasgen.apidefinition.dto.Variable

class DtoMapper {
    private val enumSchemaNames = ThreadLocal.withInitial<Set<String>> { linkedSetOf() }
    private val schemas = ThreadLocal.withInitial<Map<String, Schema<Any>>> { linkedMapOf() }
    private val oneOfVariants = ThreadLocal.withInitial<Map<String, List<OneOfVariant>>> { linkedMapOf() }
    private val oneOfParents = ThreadLocal.withInitial<Map<String, List<ConvertableName>>> { linkedMapOf() }
    private val oneOfDiscriminators = ThreadLocal.withInitial<Map<String, String?>> { linkedMapOf() }

    fun registerEnumSchemas(schemas: Map<String, Schema<Any>>) {
        this.schemas.set(schemas)
        enumSchemaNames.set(schemas
            .filterValues { it.type == "string" && !it.enum.isNullOrEmpty() }
            .keys)

        val variantsByUnion = linkedMapOf<String, List<OneOfVariant>>()
        val discriminatorsByUnion = linkedMapOf<String, String?>()
        schemas.forEach { (name, schema) ->
            if (schema !is ComposedSchema || schema.oneOf.isNullOrEmpty()) {
                return@forEach
            }
            val discriminatorMapping = schema.discriminator?.mapping.orEmpty()
            variantsByUnion[name] = schema.oneOf.mapNotNull { variantSchema ->
                val reference = variantSchema.`$ref` ?: return@mapNotNull null
                val typeName = schemaName(reference)
                val discriminatorValue = discriminatorMapping.entries
                    .firstOrNull { it.value == reference }
                    ?.key
                    ?: typeName
                OneOfVariant(discriminatorValue, ConvertableName(typeName))
            }
            discriminatorsByUnion[name] = schema.discriminator?.propertyName
        }
        oneOfVariants.set(variantsByUnion)
        oneOfDiscriminators.set(discriminatorsByUnion)

        val parentsByVariant = linkedMapOf<String, MutableList<ConvertableName>>()
        variantsByUnion.forEach { (unionName, variants) ->
            variants.forEach { variant ->
                parentsByVariant
                    .getOrPut(variant.type.value) { mutableListOf() }
                    .add(ConvertableName(unionName))
            }
        }
        oneOfParents.set(parentsByVariant)
    }

    fun isEnumSchema(name: String): Boolean = enumSchemaNames.get().contains(name)

    private fun getDto(name: String, swaggerDto: Schema<Any>): ApiModel {
        val required = linkedSetOf<String>().apply {
            addAll(swaggerDto.required.orEmpty())
        }
        val properties = linkedMapOf<String, Schema<Any>>().apply {
            putAll(swaggerDto.properties.orEmpty())
        }

        if (swaggerDto is ComposedSchema) {
            swaggerDto.allOf.orEmpty().forEach { schema ->
                @Suppress("UNCHECKED_CAST")
                val resolvedSchema = resolve(schema as Schema<Any>)
                required.addAll(resolvedSchema.required.orEmpty())
                properties.putAll(resolvedSchema.properties.orEmpty())
            }
        }

        return ApiModel(
            ConvertableName(""),
            ConvertableName(name),
            properties.map { (key, value) ->
                Variable(ConvertableName(key), getType(value, !required.contains(key)))
            },
            mutableListOf(),
            mutableListOf(),
            oneOfVariants.get()[name].orEmpty(),
            oneOfParents.get()[name].orEmpty(),
            oneOfDiscriminators.get()[name]
        )
    }

    fun map(entry: Map.Entry<String, Schema<Any>>): ApiModel = getDto(entry.key, entry.value)

    fun getType(schema: Schema<Any>?, optional: Boolean): Type {
        if (schema == null) {
            return Type(ConvertableName("Void"), optional, false)
        }
        if (schema.`$ref` != null) {
            val name = schemaName(schema.`$ref`)
            if (enumSchemaNames.get().contains(name)) {
                return Type(ConvertableName("string"), optional, false)
            }
            return Type(ConvertableName(name), optional, false)
        }
        if (schema is DateTimeSchema) {
            return Type(ConvertableName("DateTime"), optional, false)
        }
        if (schema is DateSchema) {
            return Type(ConvertableName("Date"), optional, false)
        }
        if (schema is ArraySchema) {
            @Suppress("UNCHECKED_CAST")
            return getType(schema.items as Schema<Any>, false).copy(list = true, optional = optional)
        }
        if (schema is ComposedSchema) {
            schema.allOf.orEmpty().firstOrNull()?.let {
                @Suppress("UNCHECKED_CAST")
                return getType(it as Schema<Any>, optional)
            }
            schema.oneOf.orEmpty().singleOrNull()?.let {
                @Suppress("UNCHECKED_CAST")
                return getType(it as Schema<Any>, optional)
            }
            if (!schema.oneOf.isNullOrEmpty()) {
                return Type(ConvertableName("Any"), optional, false)
            }
        }
        if (schema.type == "string" && schema.format == "uuid") {
            return Type(ConvertableName("UUID"), optional, false)
        }
        if (schema.type == null) {
            return Type(ConvertableName("Any"), optional, false)
        }
        return Type(ConvertableName(schema.type), optional, false)
    }

    fun isModel(type: String): Boolean {
        if (type == "LocalDateTime") {
            return false
        }
        return type[0].isUpperCase()
    }

    private fun resolve(schema: Schema<Any>): Schema<Any> {
        val reference = schema.`$ref` ?: return schema
        return schemas.get()[schemaName(reference)] ?: schema
    }

    private fun schemaName(reference: String): String =
        reference.replace("#/components/schemas/", "")
}
