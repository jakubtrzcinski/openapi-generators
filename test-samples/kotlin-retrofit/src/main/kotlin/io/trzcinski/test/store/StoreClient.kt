package out.io.trzcinski.test.store

import retrofit2.http.*
import retrofit2.Call

import out.io.trzcinski.test.commons.dto.Order

interface StoreClient {

    @GET("/store/inventory")
    fun getInventory(
    ): Call<Map<Object, Object>>

    @POST("/store/order")
    fun placeOrder(
    ): Call<Order>

    @GET("/store/order/{orderId}")
    fun getOrderById(
        @Path("orderId") orderId: Int
    ): Call<Order>

    @DELETE("/store/order/{orderId}")
    fun deleteOrder(
        @Path("orderId") orderId: Int
    ): Call<Void>
}