package io.trzcinski.oasgen.apidefinition.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.media.*
import io.trzcinski.oasgen.apidefinition.dto.Endpoint
import io.trzcinski.oasgen.apidefinition.dto.ApiModel
import io.trzcinski.oasgen.apidefinition.dto.Param
import io.trzcinski.oasgen.apidefinition.dto.Variable
import io.trzcinski.oasgen.apidefinition.swagger.dto.EndpointAggregate
import java.lang.Exception
import java.util.*

class EndpointAggregator {
    fun run(openApi: OpenAPI): EndpointAggregate {
        val endpoints = openApi.paths.flatMap {
            mapPathItems(it.key, it.value)
        }

        val dtos = openApi.components.schemas.map { getDto(it.key, it.value as ObjectSchema) }

        return EndpointAggregate(
            endpoints,
            dtos
        )
    }

    private fun mapPathItems(path: String, item: PathItem): List<Endpoint> {
        val endpoints: ArrayList<Endpoint> = ArrayList(8);

        if (item.get != null) endpoints.add(mapPathItems(path, "GET", item.get))
        if (item.put != null) endpoints.add(mapPathItems(path, "PUT", item.put))
        if (item.post != null) endpoints.add(mapPathItems(path, "POST", item.post))
        if (item.delete != null) endpoints.add(mapPathItems(path, "DELETE", item.delete))
        if (item.options != null) endpoints.add(mapPathItems(path, "OPTIONS", item.options))
        if (item.head != null) endpoints.add(mapPathItems(path, "HEAD", item.head))
        if (item.patch != null) endpoints.add(mapPathItems(path, "PATCH", item.patch))

        return endpoints;

    }

    fun getDto(name: String, swaggerDto: ObjectSchema): ApiModel {
        val required = swaggerDto.required ?: emptyList()
        return ApiModel(
            name,
            swaggerDto.properties.map { (key, value) ->
                val type = getType(value);
                val collection = value is ArraySchema;
                Variable(key, getType(value), required.contains(key), collection, isModel(type))
            },
            listOf(),
            listOf()
        )
    }

    fun mapPathItems(path: String, method: String, item: Operation): Endpoint {
        val params: MutableList<Param> = if (item.parameters == null) mutableListOf() else item.parameters
            .map {
                val type = getType(it.schema)
                Param(
                    it.`in`,
                    it.name,
                    type,
                    isModel(type)
                )
            }.toMutableList()

        val req = getBodyType(item)
        if (req != null) {
            params.add(0, Param("body", "payload", req, true))
        }
        var name = item.operationId
        if (name.contains("Using")) {
            name = name.substring(0, name.indexOf("Using"));
        }
        val response = getResponseType(item);
        return Endpoint(
            path,
            name,
            method,
            params,
            response,
            response.isNotEmpty() && isModel(response)
        )
    }

    private fun isModel(type: String) : Boolean {
        if(type == "LocalDateTime"){
            return false
        }
        return type[0].isUpperCase()
    }

    private fun getBodyType(operation: Operation): String? {
        return try {
            val schema = operation.requestBody.content["application/json"]!!.schema

            getType(schema)

        } catch (ex: Exception) {
            null
        }
    }

    private fun getType(schema: Schema<Any>?): String {
        if (schema == null) return "void";
        if (schema!!.`$ref` != null) {
            return schema!!.`$ref`.replace("#/components/schemas/", "")

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

    private fun getResponseType(operation: Operation): String {
        try {
            if (!operation.responses.containsKey("200")) {
                return "void"
            }
            val schema = operation.responses.get("200")!!.content["*/*"]!!.schema

            return getType(schema)

        } catch (ex: Exception) {
            return "void"
        }
    }

}