package io.trzcinski.oasgen.apidefinition.dto

import com.improve_future.case_changer.toSnakeCase

/**
 * String wrapper
 */
class ConvertableName {
    val value: String

    constructor(value: String) {
        this.value = value.split("-").map { it.capitalize() }.joinToString("")
        this.kebabCaseLowercase = this.value.capitalize().toSnakeCase().replace("_", "-").toLowerCase()
        this.kebabCaseUppercase = this.value.capitalize().toSnakeCase().replace("_", "-").toUpperCase()
        this.snakeCaseLowercase = this.value.capitalize().toSnakeCase().replace("-", "_").toLowerCase()
        this.snakeCaseUppercase = this.value.capitalize().toSnakeCase().replace("-", "_").toUpperCase()
        this.camelCase = this.value.decapitalize()
        this.pascalCase = this.value.capitalize()
    }

    /**
     *  eg. user-api-model
     */
    val kebabCaseLowercase: String

    /**
     * eg. USER-API-MODEL
     */
    val kebabCaseUppercase: String

    /**
     * eg. user_api_model
     */
    val snakeCaseLowercase: String

    /**
     * eg. USER_API_MODEL
     */
    val snakeCaseUppercase: String

    /**
     * eg. userApiModel
     */
    val camelCase: String

    /**
     * eg. UserApiModel
     */
    val pascalCase: String


    override fun toString(): String {
        return value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConvertableName

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}