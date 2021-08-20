package io.trzcinski.oasgen.apidefinition.dto

/**
 * String wrapper
 */
class ConvertableName(
    private val value: String
) {
    /**
     *  eg. user-api-model
     */
    val kebabCaseLowercased: String = "todo"

    /**
     * eg. USER-API-MODEL
     */
    val kebabCaseUppercased: String = "todo"
    /**
     * eg. user_api_model
     */
    val snakeCaseLowercased: String = "todo"
    /**
     * eg. USER_API_MODEL
     */
    val snakeCaseUppercased: String = "todo"
    /**
     * eg. userApiModel
     */
    val camelCase: String = "todo"
    /**
     * eg. UserApiModel
     */
    val pascalCase: String = "todo"


    override fun toString(): String {
        return value
    }
}