package io.trzcinski.oasgen.apidefinition.swagger

import io.trzcinski.oasgen.apidefinition.dto.CRUDAggregate
import io.trzcinski.oasgen.apidefinition.dto.CRUD
import io.trzcinski.oasgen.apidefinition.swagger.dto.EndpointAggregate
import io.trzcinski.oasgen.apidefinition.dto.ApiModel
import io.trzcinski.oasgen.apidefinition.dto.Endpoint
import io.trzcinski.oasgen.apidefinition.dto.ExternalImport
import io.trzcinski.oasgen.utils.StringUtils


class CRUDAggregator(
    private val pathPrefix: String
) {
    fun run(endpointAggregate: EndpointAggregate): CRUDAggregate {
        val crudsRaw = getCruds(endpointAggregate)
        val commonDtos = getCommonDtos(crudsRaw, endpointAggregate.apiModels)

        val commonDtoNames = commonDtos.map { it.name }
        val crudsDtos = crudsRaw
            .mapValues { endpoints ->
                getDTOS(endpoints.value, endpointAggregate.apiModels)
                    .flatMap { expand(it, endpointAggregate.apiModels) }
                    .map { it.name }
                    .filter { !commonDtoNames.contains(it) }
                    .distinct()
            }.toMutableMap()
        crudsDtos.put("Commons", commonDtos.map { it.name })

        val cruds = crudsRaw.map {
            val referingDtos = getDTOS(it.value, endpointAggregate.apiModels)
                .flatMap { expand(it, endpointAggregate.apiModels) }.distinct()
            val commons = referingDtos.filter { dto -> commonDtos.contains(dto) }.map { it.name }.distinct()

            val dtos = endpointAggregate.apiModels.filter { dto -> crudsDtos[it.key]!!.contains(dto.name) }.map { dto ->
                addReferences(
                    dto,
                    it.key,
                    crudsDtos,
                    commons
                )
            }

            val imports = getImports(it.value, crudsDtos, commons);
            CRUD(
                it.key,
                it.value,
                dtos,
                imports.filter { import-> import.crud == it.key }.map { it.name },
                imports.filter { import-> import.crud != it.key },
            )
        }
        return CRUDAggregate(
            cruds,
            commonDtos
        )
    }

    private fun addReferences(apiModel: ApiModel, crud: String, cruds: Map<String, List<String>>, commons: List<String>): ApiModel {
        val imports = apiModel.properties.mapNotNull { findReference(it.type, cruds, commons) }.distinct()
        return ApiModel(
            apiModel.name,
            apiModel.properties,
            imports.filter { it.crud == crud }.map { it.name },
            imports.filter { it.crud != crud },
        )
    }

    private fun getImports(endpoints: List<Endpoint>, cruds: Map<String, List<String>>, commons: List<String>): List<ExternalImport> {
        val dto = endpoints
            .flatMap { listOf(listOf(it.responseType), it.params.map { param -> param.type }).flatten() }
        return dto.mapNotNull { findReference(it, cruds, commons) }.distinct()
    }


    private fun findReference(dto: String, cruds: Map<String, List<String>>, commons: List<String>): ExternalImport? {
        for (crud in cruds) {
            for (cDto in crud.value) {
                if (dto == cDto) {
                    return ExternalImport(crud.key, dto);
                }
            }
        }
        if (commons.contains(dto)) {
            return ExternalImport("Commons", dto)
        }
        return null;
    }

    private fun getCruds(endpointAggregate: EndpointAggregate): Map<String, ArrayList<Endpoint>> {
        val cruds = HashMap<String, ArrayList<Endpoint>>()
        endpointAggregate.endpoints.forEach {
            val crudName = getCrudName(it.path)
            if (!cruds.containsKey(crudName)) {
                cruds[crudName] = ArrayList()
            }
            cruds[crudName]!!.add(it.copy(path = pathPrefix+it.path))
        }
        return cruds
    }

    private fun getCommonDtos(cruds: Map<String, ArrayList<Endpoint>>, allApiModels: List<ApiModel>): List<ApiModel> {
        return allApiModels.filter { cruds.keys.filter { crud -> it.name.contains(crud) }.isEmpty() }
    }

    private fun getDTOS(endpoints: List<Endpoint>, allApiModels: List<ApiModel>): List<ApiModel> {
        return endpoints
            .flatMap {
                listOf(listOf(it.responseType), it.params.map { it.type }).flatten()
                    .mapNotNull { allApiModels.filter { dto -> dto.name == it }.firstOrNull() }
            }
    }

    private fun expand(apiModel: ApiModel, apiModels: List<ApiModel>): List<ApiModel> {
        val types = apiModel.properties.map { it.type }
        val ret = apiModels.filter { types.contains(it.name) }.flatMap { expand(it, apiModels) }.toMutableList()
        ret.add(apiModel)
        return ret
    }

    private fun getCrudName(rawPath: String): String {
        val path = if (rawPath.startsWith("/")) rawPath.substring(1) else rawPath
        if (path.isBlank()) return "Root"
        if (path == "/") return "Root"


        if (!path.contains("/")) {
            return StringUtils.kebabToCamel(path).capitalize()
        }
        return StringUtils.kebabToCamel(
            path.substring(0, path.indexOf("/")).capitalize()
        )
    }

}