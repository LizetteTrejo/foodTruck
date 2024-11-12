package mx.ipn.escom.trejol.proyectocomposemoviles.services

import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.auth.SignUpResponse
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.orders.OrderDetailResult
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.orders.OrderResponse
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.products.Data
import mx.ipn.escom.trejol.proyectocomposemoviles.utils.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrdersService {

    @FormUrlEncoded
    @POST("/api/order")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Field("products") products:String,
        @Field("delivery_location_id") delivery_location_id: Int,
        @Field("type_delivery") type_delivery: Int,
    ): SignUpResponse

    @GET("/api/order")
    suspend fun getOrders(
        @Header("Authorization") token: String,
        @Query("is_deliver") is_deliver: String? = null,
    ): OrderResponse

    @FormUrlEncoded
    @PUT("/api/order/changeStatus/{order_id}")
    suspend fun changeStatus(
        @Header("Authorization") token: String,
        @Field("status") status: Int,
        @Path("order_id") order_id: Int
    ): SignUpResponse

    @GET("/api/order/{order_id}")
    suspend fun getOrderDetail(
        @Header("Authorization") token: String,
        @Path("order_id") order_id: Int
    ): OrderDetailResult

    @FormUrlEncoded
    @PUT("/api/order/assignDelivery/{order_id}")
    suspend fun assignDelivery(
        @Header("Authorization") token: String,
        @Field("deliver_id") deliver_id: Int,
        @Path("order_id") order_id: Int
    ): SignUpResponse

}

object RetrofitClientOrders {

    fun makeRetrofitService(): OrdersService {
        return Retrofit.Builder()
            .baseUrl(Config.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(OrdersService::class.java)

    }
}