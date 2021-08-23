package io.trzcinski.oasgen.apidefinition.swagger.mapper

import io.swagger.v3.oas.models.media.*
import io.trzcinski.oasgen.apidefinition.dto.ApiModel
import io.trzcinski.oasgen.apidefinition.dto.ConvertableName
import io.trzcinski.oasgen.apidefinition.dto.Variable

class DtoMapper {
    private fun getDto(name: String, swaggerDto: ObjectSchema): ApiModel {
        val required = swaggerDto.required ?: emptyList()
        return ApiModel(
            ConvertableName(name),
            swaggerDto.properties.map { (key, value) ->
                val type = getType(value)
                val collection = value is ArraySchema
                Variable(ConvertableName(key), ConvertableName(getType(value)), required.contains(key), collection, isModel(type))
            },
            listOf(),
            listOf()
        )
    }

    fun map(entry: Map.Entry<String, Schema<Any>>) : ApiModel{
        return getDto(
            entry.key,
            entry.value as ObjectSchema
        )

    }

    fun getType(schema: Schema<Any>?): String {
        if (schema == null) return "void"
        if (schema.`$ref` != null) {
            return schema.`$ref`.replace("#/components/schemas/", "")

        }
        if (schema is DateTimeSchema) {
            return "DateTime"
        }
        if (schema is DateSchema) {
            return "DateTime"
        }
        if (schema is ArraySchema) {
            return getType(schema.items as Schema<Any>)
        }

        return schema.type
    }


    fun isModel(type: String) : Boolean {
        if(type == "LocalDateTime"){
            return false
        }
        return type[0].isUpperCase()
    }

}