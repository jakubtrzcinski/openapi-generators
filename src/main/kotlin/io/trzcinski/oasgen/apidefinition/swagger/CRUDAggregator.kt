package io.trzcinski.oasgen.apidefinition.swagger

import io.trzcinski.oasgen.apidefinition.dto.*
import io.trzcinski.oasgen.apidefinition.swagger.dto.EndpointAggregate


class CRUDAggregator {
    fun run(endpointAggregate: EndpointAggregate): CRUDAggregate {
        val crudsRaw = getCruds(endpointAggregate).toMutableMap()
        crudsRaw[ConvertableName("Commons")] = ArrayList()

        var crudNames = crudsRaw.keys.sortedByDescending { it.value.length }
        for (apiModel in endpointAggregate.apiModels) {
            for (crudName in crudNames) {
                if(apiModel.name.pascalCase.contains(crudName.pascalCase)){
                    apiModel.origin = crudName;
                    break;
                }
            }
        }
        for (apiModel in endpointAggregate.apiModels) {
            if(apiModel.origin.pascalCase == ""){
                apiModel.origin = ConvertableName("Commons")
            }
        }
        val apiModelByCrudMap = endpointAggregate.apiModels.groupBy { it.origin }.mapValues { it.value.map { v -> v.name } }

        val cruds = crudsRaw.map {
            val crudsDtos = endpointAggregate.apiModels.filter { import -> import.origin ==  it.key }
            val imports = getImports(it.value, apiModelByCrudMap)
            CRUD(
                it.key,
                it.value,
                crudsDtos,
                imports.filter { import-> import.crud == it.key }.map { it.name },
                imports.filter { import-> import.crud != it.key },
            )
        }
        endpointAggregate.apiModels.forEach { addReferences(it, apiModelByCrudMap) }

        return CRUDAggregate(
            cruds
        )
    }

    private fun addReferences(apiModel: ApiModel, cruds: Map<ConvertableName, List<ConvertableName>>) {
        val imports = apiModel.properties.mapNotNull { findReference(it.type, cruds,) }.distinct()
        apiModel.ownCrudImports.addAll(imports.filter { it.crud.value == apiModel.origin.value  }.map { it.name })
        apiModel.externalCrudImports.addAll(imports.filter { it.crud.value  != apiModel.origin.value  })
    }

    private fun getImports(endpoints: List<Endpoint>, cruds: Map<ConvertableName, List<ConvertableName>>): List<ExternalImport> {
        val dto = endpoints
            .flatMap { listOf(listOf(it.responseType), it.params.map { param -> param.type }).flatten() }
        return dto.mapNotNull { findReference(it, cruds) }.distinct()
    }


    private fun findReference(dto: Type, cruds: Map<ConvertableName, List<ConvertableName>>): ExternalImport? {
        for (crud in cruds) {
            for (cDto in crud.value) {
                if (dto.name == cDto) {
                    return ExternalImport(crud.key, dto.name)
                }
            }
        }
        return null
    }

    private fun getCruds(endpointAggregate: EndpointAggregate): Map<ConvertableName, ArrayList<Endpoint>> {
        val cruds = HashMap<ConvertableName, ArrayList<Endpoint>>()
        endpointAggregate.endpoints.forEach {
            val crud = convertableNameFromUrl(it.path)
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
            return ConvertableName(path)
        }
        return ConvertableName(
            path.substring(0, path.indexOf("/")).replace("/", "")
        )
    }
}