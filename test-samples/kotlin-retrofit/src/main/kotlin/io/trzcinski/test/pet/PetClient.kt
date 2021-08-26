package out.io.trzcinski.test.pet

import retrofit2.http.*
import retrofit2.Call

import out.io.trzcinski.test.pet.dto.Pet
import out.io.trzcinski.test.commons.dto.ApiResponse

interface PetClient {

    @POST("/pet/{petId}/uploadImage")
    fun uploadFile(
        @Path("petId") petId: Int
    ): Call<ApiResponse>

    @PUT("/pet")
    fun updatePet(
    ): Call<Void>

    @POST("/pet")
    fun addPet(
    ): Call<Void>

    @GET("/pet/findByStatus")
    fun findPetsByStatus(
        @Query("status") status: List<String>
    ): Call<List<Pet>>

    @GET("/pet/findByTags")
    fun findPetsByTags(
        @Query("tags") tags: List<String>
    ): Call<List<Pet>>

    @GET("/pet/{petId}")
    fun getPetById(
        @Path("petId") petId: Int
    ): Call<Pet>

    @POST("/pet/{petId}")
    fun updatePetWithForm(
        @Path("petId") petId: Int
    ): Call<Void>

    @DELETE("/pet/{petId}")
    fun deletePet(
        @Header("api_key") api_key: String,
        @Path("petId") petId: Int
    ): Call<Void>
}