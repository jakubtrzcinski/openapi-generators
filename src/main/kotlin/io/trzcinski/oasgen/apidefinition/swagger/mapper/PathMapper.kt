package io.trzcinski.oasgen.apidefinition.swagger.mapper

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.trzcinski.oasgen.apidefinition.dto.ConvertableName
import io.trzcinski.oasgen.apidefinition.dto.Endpoint
import io.trzcinski.oasgen.apidefinition.dto.Param
import io.trzcinski.oasgen.apidefinition.dto.Type

class PathMapper(
    private val dtoMapper: DtoMapper
) {
    fun mapPathItems(path: String, item: PathItem): List<Endpoint> {
        val endpoints: ArrayList<Endpoint> = ArrayList(8)

        if (item.get != null) endpoints.add(mapPathItems(path, "GET", item.get))
        if (item.put != null) endpoints.add(mapPathItems(path, "PUT", item.put))
        if (item.post != null) endpoints.add(mapPathItems(path, "POST", item.post))
        if (item.delete != null) endpoints.add(mapPathItems(path, "DELETE", item.delete))
        if (item.options != null) endpoints.add(mapPathItems(path, "OPTIONS", item.options))
        if (item.head != null) endpoints.add(mapPathItems(path, "HEAD", item.head))
        if (item.patch != null) endpoints.add(mapPathItems(path, "PATCH", item.patch))

        return endpoints

    }

    private fun mapPathItems(path: String, method: String, item: Operation): Endpoint {
        val params: MutableList<Param> = if (item.parameters == null) mutableListOf() else item.parameters
            .map {
                Param(
                    it.`in`,
                    ConvertableName(it.name),
                    it.name,
                    dtoMapper.getType(it.schema, false)
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
            path,
            ConvertableName(name),
            method,
            params,
            getResponseType(item)
        )
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
                ?: operation.responses["default"]
                ?: return Type(ConvertableName("Void"), false, false)
            val schema = responses.content["*/*"]?.schema ?: responses.content["application/json"]?.schema

            return dtoMapper.getType(schema, false)

        } catch (ex: Exception) {
            return Type(ConvertableName("Void"), false, false)
        }
    }

}