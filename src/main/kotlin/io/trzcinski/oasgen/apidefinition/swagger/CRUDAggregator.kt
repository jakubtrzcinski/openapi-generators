package io.trzcinski.oasgen.apidefinition.swagger

import io.trzcinski.oasgen.apidefinition.dto.*
import io.trzcinski.oasgen.apidefinition.swagger.dto.EndpointAggregate
import io.trzcinski.oasgen.utils.StringUtils


class CRUDAggregator() {
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
        crudsDtos.put(ConvertableName("Commons"), commonDtos.map { it.name })

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
        }.toMutableList()
        if(cruds.findLast { it.name.value == "Commons" } == null){
            cruds.add(CRUD(ConvertableName("Commons"), listOf(), commonDtos, listOf(), listOf()))
        }
        return CRUDAggregate(
            cruds,
            commonDtos
        )
    }

    private fun addReferences(apiModel: ApiModel, crud: ConvertableName, cruds: Map<ConvertableName, List<ConvertableName>>, commons: List<ConvertableName>): ApiModel {
        val imports = apiModel.properties.mapNotNull { findReference(it.type, cruds, commons) }.distinct()
        return ApiModel(
            apiModel.name,
            apiModel.properties,
            imports.filter { it.crud.value == crud.value  }.map { it.name },
            imports.filter { it.crud.value  != crud.value  },
        )
    }

    private fun getImports(endpoints: List<Endpoint>, cruds: Map<ConvertableName, List<ConvertableName>>, commons: List<ConvertableName>): List<ExternalImport> {
        val dto = endpoints
            .flatMap { listOf(listOf(it.responseType), it.params.map { param -> param.type }).flatten() }
        return dto.mapNotNull { findReference(it, cruds, commons) }.distinct()
    }


    private fun findReference(dto: ConvertableName, cruds: Map<ConvertableName, List<ConvertableName>>, commons: List<ConvertableName>): ExternalImport? {
        for (crud in cruds) {
            for (cDto in crud.value) {
                if (dto == cDto) {
                    return ExternalImport(crud.key, dto);
                }
            }
        }
        if (commons.contains(dto)) {
            return ExternalImport(ConvertableName("Commons"), dto)
        }
        return null;
    }

    private fun getCruds(endpointAggregate: EndpointAggregate): Map<ConvertableName, ArrayList<Endpoint>> {
        val cruds = HashMap<ConvertableName, ArrayList<Endpoint>>()
        endpointAggregate.endpoints.forEach {
            var crud = convertableNameFromUrl(it.path)
            if (!cruds.containsKey(crud)) {
                cruds[crud] = ArrayList()
            }
            cruds[crud]!!.add(it)
        }
        return cruds
    }

    private fun convertableNameFromUrl(rawPath: String) : ConvertableName {
        if(rawPath == "/"){
            return ConvertableName("Commons")
        }
        val path = rawPath.substring(1)
        if(!path.contains("/")){
            return ConvertableName(path);
        }
        return ConvertableName(
            path.substring(0, path.indexOf("/")).replace("/", "")
        )
    }

    private fun getCommonDtos(cruds: Map<ConvertableName, ArrayList<Endpoint>>, allApiModels: List<ApiModel>): List<ApiModel> {
        return allApiModels.filter { cruds.keys.filter { crud -> isFromCrud(it, crud) }.isEmpty() }
    }

    private fun isFromCrud(it: ApiModel, crud: ConvertableName) : Boolean {
        return it.name.value.toLowerCase().contains(crud.value.toLowerCase())
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