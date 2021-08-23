package io.trzcinski.oasgen.apidefinition.dto

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ConvertableNameTest {

    private val convertableName = ConvertableName("PetStore")

    @Test
    fun getKebabCaseLowercased() {
        assertEquals("pet-store", convertableName.kebabCaseLowercase)
    }

    @Test
    fun getKebabCaseUppercased() {
        assertEquals("PET-STORE", convertableName.kebabCaseUppercase)
    }

    @Test
    fun getSnakeCaseLowercased() {
        assertEquals("pet_store", convertableName.snakeCaseLowercase)
    }

    @Test
    fun getSnakeCaseUppercased() {
        assertEquals("PET_STORE", convertableName.snakeCaseUppercase)
    }

    @Test
    fun getCamelCase() {
        assertEquals("petStore", convertableName.camelCase)
    }

    @Test
    fun getPascalCase() {
        assertEquals("PetStore", convertableName.pascalCase)
    }

    @Test
    fun testToString() {
        assertEquals("PetStore", convertableName.toString())
    }
}