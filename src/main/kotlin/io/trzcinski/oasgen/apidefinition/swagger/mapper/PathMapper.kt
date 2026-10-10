package io.trzcinski.oasgen.apidefinition.swagger.mapper

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.parameters.Parameter
import io.trzcinski.oasgen.apidefinition.dto.ConvertableName
import io.trzcinski.oasgen.apidefinition.dto.Endpoint
import io.trzcinski.oasgen.apidefinition.dto.Param
import io.trzcinski.oasgen.apidefinition.dto.Type

class PathMapper(
    private val dtoMapper: DtoMapper
) {
    fun mapPathItems(basePath: String, path: String, item: PathItem): List<Endpoint> {
        val endpoints: ArrayList<Endpoint> = ArrayList(8)

        val inheritedParameters = item.parameters.orEmpty()
        if (item.get != null) endpoints.add(mapPathItems(basePath, path, "GET", item.get, inheritedParameters))
        if (item.put != null) endpoints.add(mapPathItems(basePath, path, "PUT", item.put, inheritedParameters))
        if (item.post != null) endpoints.add(mapPathItems(basePath, path, "POST", item.post, inheritedParameters))
        if (item.delete != null) endpoints.add(mapPathItems(basePath, path, "DELETE", item.delete, inheritedParameters))
        if (item.options != null) endpoints.add(mapPathItems(basePath, path, "OPTIONS", item.options, inheritedParameters))
        if (item.head != null) endpoints.add(mapPathItems(basePath, path, "HEAD", item.head, inheritedParameters))
        if (item.patch != null) endpoints.add(mapPathItems(basePath, path, "PATCH", item.patch, inheritedParameters))

        return endpoints

    }

    private fun mapPathItems(basePath: String, path: String, method: String, item: Operation, inheritedParameters: List<Parameter>): Endpoint {
        val operationParameters = item.parameters.orEmpty()
        val overriddenNames = operationParameters.mapNotNull { it.name }.toSet()
        val params = (operationParameters + inheritedParameters.filter { it.name !in overriddenNames })
            .filter { it.`in` != null && it.name != null && it.schema != null }
            .map {
                Param(
                    it.`in`,
                    ConvertableName(it.name),
                    it.name,
                    dtoMapper.getType(it.schema, it.required != true)
                )
            }.toMutableList()

        val req = getBodyType(item)
        if (req != null) {
            params.add(0, Param("body", ConvertableName("payload"), "payload", req))
        }
        var name = item.operationId
        if (name.contains("Using")) {
            name = name.substring(0, name.indexOf("Using"))
        }
        return Endpoint(
            basePath,
            path,
            ConvertableName(name),
            method,
            params,
            getResponseType(item),
            getGroup(item)
        )
    }

    private fun getGroup(operation: Operation): ConvertableName? {
        val value = operation.extensions?.get("x-oasgen-group") ?: return null
        require(value is String && value.matches(Regex("[A-Za-z_][A-Za-z0-9_-]*"))) {
            "x-oasgen-group must be a non-empty identifier for ${operation.operationId}"
        }
        return ConvertableName(value)
    }

    private fun getBodyType(operation: Operation): Type? {
        return try {
            val schema = operation.requestBody.content["application/json"]!!.schema

            val required = schema.required ?: emptyList()
            dtoMapper.getType(schema, required.contains(schema.name))

        } catch (ex: Exception) {
            null
        }
    }



    private fun getResponseType(operation: Operation): Type {
        try {
            val responses = operation.responses["200"]
                ?: operation.responses["201"]
                ?: operation.responses["default"]
                ?: return Type(ConvertableName("Void"), false, false)
            val schema = responses.content["*/*"]?.schema ?: responses.content["application/json"]?.schema

            return dtoMapper.getType(schema, false)

        } catch (ex: Exception) {
            return Type(ConvertableName("Void"), false, false)
        }
    }

}
