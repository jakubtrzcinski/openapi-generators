package io.trzcinski.oasgen.apidefinition.swagger.mapper

import io.swagger.v3.oas.models.media.*
import io.trzcinski.oasgen.apidefinition.dto.ApiModel
import io.trzcinski.oasgen.apidefinition.dto.ConvertableName
import io.trzcinski.oasgen.apidefinition.dto.Type
import io.trzcinski.oasgen.apidefinition.dto.Variable

class DtoMapper {
    private fun getDto(name: String, swaggerDto: ObjectSchema): ApiModel {
        val required = swaggerDto.required ?: emptyList()
        return ApiModel(
            ConvertableName(name),
            swaggerDto.properties.map { (key, value) ->
                Variable(ConvertableName(key), getType(value, !required.contains(key)))
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

    fun getType(schema: Schema<Any>?, optional: Boolean): Type {
        if (schema == null){
            return Type(ConvertableName("Void"), optional, false)
        }
        if (schema.`$ref` != null) {
            return Type(ConvertableName(schema.`$ref`.replace("#/components/schemas/", "")), optional, false)

        }
        if (schema is DateTimeSchema) {
            return Type(ConvertableName("DateTime"), optional, false)
        }
        if (schema is DateSchema) {
            return Type(ConvertableName("Date"), optional, false)
        }
        if (schema is ArraySchema) {
            return getType(schema.items as Schema<Any>, false).copy(list = true, optional = optional)
        }

        return Type(ConvertableName(schema.type), optional, false)
    }


    fun isModel(type: String) : Boolean {
        if(type == "LocalDateTime"){
            return false
        }
        return type[0].isUpperCase()
    }

}