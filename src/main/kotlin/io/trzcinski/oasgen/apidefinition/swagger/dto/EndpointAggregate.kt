package io.trzcinski.oasgen.apidefinition.swagger.dto

import io.trzcinski.oasgen.apidefinition.dto.ApiModel
import io.trzcinski.oasgen.apidefinition.dto.Endpoint

data class EndpointAggregate(
    val endpoints: List<Endpoint>,
    val apiModels: List<ApiModel>
)