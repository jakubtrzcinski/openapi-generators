package io.trzcinski.oasgen.apidefinition.swagger.mapper

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.trzcinski.oasgen.apidefinition.dto.ConvertableName
import io.trzcinski.oasgen.apidefinition.dto.Endpoint
import io.trzcinski.oasgen.apidefinition.dto.Param

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
                val type = dtoMapper.getType(it.schema)
                Param(
                    it.`in`,
                    ConvertableName(it.name),
                    ConvertableName(type),
                    dtoMapper.isModel(type)
                )
            }.toMutableList()

        val req = getBodyType(item)
        if (req != null) {
            params.add(0, Param("body", ConvertableName("payload"), ConvertableName(req), true))
        }
        var name = item.operationId
        if (name.contains("Using")) {
            name = name.substring(0, name.indexOf("Using"))
        }
        val response = getResponseType(item)
        return Endpoint(
            path,
            ConvertableName(name),
            method,
            params,
            ConvertableName(response),
            response.isNotEmpty() && dtoMapper.isModel(response)
        )
    }

    private fun getBodyType(operation: Operation): String? {
        return try {
            val schema = operation.requestBody.content["application/json"]!!.schema

            dtoMapper.getType(schema)

        } catch (ex: Exception) {
            null
        }
    }



    private fun getResponseType(operation: Operation): String {
        try {
            if (!operation.responses.containsKey("200")) {
                return "void"
            }
            val schema = operation.responses["200"]!!.content["*/*"]!!.schema

            return dtoMapper.getType(schema)

        } catch (ex: Exception) {
            return "void"
        }
    }

}