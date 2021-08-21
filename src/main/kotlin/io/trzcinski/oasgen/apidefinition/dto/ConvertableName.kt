package io.trzcinski.oasgen.apidefinition.dto

import com.improve_future.case_changer.toSnakeCase

/**
 * String wrapper
 */
class ConvertableName(
    val value: String
) {
    /**
     *  eg. user-api-model
     */
    val kebabCaseLowercase: String = value.capitalize().toSnakeCase().replace("_", "-").toLowerCase()

    /**
     * eg. USER-API-MODEL
     */
    val kebabCaseUppercase: String = value.capitalize().toSnakeCase().replace("_", "-").toUpperCase()
    /**
     * eg. user_api_model
     */
    val snakeCaseLowercase: String = value.capitalize().toSnakeCase().replace("-", "_").toLowerCase()
    /**
     * eg. USER_API_MODEL
     */
    val snakeCaseUppercase: String = value.capitalize().toSnakeCase().replace("-", "_").toUpperCase()
    /**
     * eg. userApiModel
     */
    val camelCase: String = value.decapitalize()
    /**
     * eg. UserApiModel
     */
    val pascalCase: String = value.capitalize()


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