package out.io.trzcinski.test.pet

import retrofit2.http.*
import retrofit2.Call

import out.io.trzcinski.test.pet.dto.Pet

interface PetClient {

        @POST("/pet/{petId}/uploadImage")
        fun uploadFile(
             petId: Int
        ): Call<Void>

        @PUT("/pet")
        fun updatePet(
             payload: Pet
        ): Call<Void>

        @POST("/pet")
        fun addPet(
             payload: Pet
        ): Call<Void>

        @GET("/pet/findByStatus")
        fun findPetsByStatus(
             status: String
        ): Call<Void>

        @GET("/pet/findByTags")
        fun findPetsByTags(
             tags: String
        ): Call<Void>

        @GET("/pet/{petId}")
        fun getPetById(
             petId: Int
        ): Call<Void>

        @POST("/pet/{petId}")
        fun updatePetWithForm(
             petId: Int
        ): Call<Void>

        @DELETE("/pet/{petId}")
        fun deletePet(
             api_key: String,
             petId: Int
        ): Call<Void>
}