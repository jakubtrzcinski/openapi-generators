package out.io.trzcinski.test.store

import retrofit2.http.*
import retrofit2.Call

import out.io.trzcinski.test.commons.dto.Order

interface StoreClient {

        @GET("/store/inventory")
        fun getInventory(
        ): Call<Void>

        @POST("/store/order")
        fun placeOrder(
             payload: Order
        ): Call<Void>

        @GET("/store/order/{orderId}")
        fun getOrderById(
             orderId: Int
        ): Call<Void>

        @DELETE("/store/order/{orderId}")
        fun deleteOrder(
             orderId: Int
        ): Call<Void>
}