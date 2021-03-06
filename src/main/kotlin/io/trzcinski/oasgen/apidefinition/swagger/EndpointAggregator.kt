package io.trzcinski.oasgen.apidefinition.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.trzcinski.oasgen.apidefinition.swagger.dto.EndpointAggregate
import io.trzcinski.oasgen.apidefinition.swagger.mapper.DtoMapper
import io.trzcinski.oasgen.apidefinition.swagger.mapper.PathMapper

class EndpointAggregator(
    private val pathMapper: PathMapper,
    private val dtoMapper: DtoMapper
) {
    fun run(openApi: OpenAPI): EndpointAggregate {
        val base = openApi.servers
            .firstOrNull()
            ?.url
            ?.replace(Regex("(//|)(http(s|)://)(www.|).+/"), "")
            ?.let { "/$it" }
            ?: ""
        val endpoints = openApi.paths.flatMap {
            pathMapper.mapPathItems(base, it.key, it.value)
        }

        val dtos = openApi
            .components
            .schemas
            .map { dtoMapper.map(it) }

        return EndpointAggregate(
            endpoints,
            dtos
        )
    }










}