package out.shared.api.user

import retrofit2.http.*
import retrofit2.Call

import out.shared.api.user.dto.User

interface UserClient {

        @POST("/user/createWithList")
        fun createUsersWithListInput(
             payload: User
        ): Call<Void>

        @GET("/user/{username}")
        fun getUserByName(
             username: String
        ): Call<Void>

        @PUT("/user/{username}")
        fun updateUser(
             payload: User,
             username: String
        ): Call<Void>

        @DELETE("/user/{username}")
        fun deleteUser(
             username: String
        ): Call<Void>

        @GET("/user/login")
        fun loginUser(
             username: String,
             password: String
        ): Call<Void>

        @GET("/user/logout")
        fun logoutUser(
        ): Call<Void>

        @POST("/user/createWithArray")
        fun createUsersWithArrayInput(
             payload: User
        ): Call<Void>

        @POST("/user")
        fun createUser(
             payload: User
        ): Call<Void>
}