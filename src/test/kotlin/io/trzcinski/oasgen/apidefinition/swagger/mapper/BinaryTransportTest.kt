package io.trzcinski.oasgen.apidefinition.swagger.mapper

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.media.*
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import kotlin.test.*

class BinaryTransportTest {
    private val mapper = PathMapper(DtoMapper())

    @Test
    fun `multipart fields preserve names requiredness and binary cardinality`() {
        val schema = ObjectSchema()
            .addProperties("file", BinarySchema())
            .addProperties("files", ArraySchema().items(BinarySchema()))
            .addProperties("accessMode", StringSchema())
            .required(listOf("file", "files"))
        val operation = Operation().operationId("upload").requestBody(
            RequestBody().required(true).content(Content().addMediaType(
                "multipart/form-data", MediaType().schema(schema)
            ))
        )
        val endpoint = mapper.mapPathItems("", "/files", PathItem().post(operation)).single()
        assertTrue(endpoint.multipart)
        assertEquals(listOf("file", "files", "accessMode"), endpoint.params.map { it.alias })
        assertTrue(endpoint.params.all { it.place == "multipart" })
        assertEquals(listOf("MultipartFile", "MultipartFile", "String"), endpoint.params.map { it.type.name.pascalCase })
        assertFalse(endpoint.params[0].type.list)
        assertTrue(endpoint.params[1].type.list)
        assertFalse(endpoint.params[0].type.optional)
        assertTrue(endpoint.params[2].type.optional)
    }

    @Test
    fun `binary responses retain bytes for any advertised binary media type`() {
        for (mediaType in listOf("application/octet-stream", "image/*", "application/pdf")) {
            val operation = Operation().operationId("download").responses(ApiResponses().addApiResponse(
                "200", ApiResponse().content(Content().addMediaType(mediaType, MediaType().schema(BinarySchema())))
            ))
            val endpoint = mapper.mapPathItems("", "/files", PathItem().get(operation)).single()
            assertEquals("Binary", endpoint.responseType.name.pascalCase)
            assertFalse(endpoint.responseType.list)
        }
    }
}
