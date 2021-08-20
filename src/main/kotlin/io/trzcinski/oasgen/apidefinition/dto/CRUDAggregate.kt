package io.trzcinski.oasgen.apidefinition.dto

data class CRUDAggregate(
    val cruds: List<CRUD>,
    val commonApiModels: List<ApiModel>
)