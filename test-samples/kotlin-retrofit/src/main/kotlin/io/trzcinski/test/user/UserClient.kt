package out.io.trzcinski.test.user

import retrofit2.http.*
import retrofit2.Call

import out.io.trzcinski.test.user.dto.User

interface UserClient {

    @POST("/user/createWithList")
    fun createUsersWithListInput(
    ): Call<Void>

    @GET("/user/{username}")
    fun getUserByName(
        @Path("username") username: String
    ): Call<User>

    @PUT("/user/{username}")
    fun updateUser(
        @Path("username") username: String
    ): Call<Void>

    @DELETE("/user/{username}")
    fun deleteUser(
        @Path("username") username: String
    ): Call<Void>

    @GET("/user/login")
    fun loginUser(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<String>

    @GET("/user/logout")
    fun logoutUser(
    ): Call<Void>

    @POST("/user/createWithArray")
    fun createUsersWithArrayInput(
    ): Call<Void>

    @POST("/user")
    fun createUser(
    ): Call<Void>
}