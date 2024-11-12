package mx.ipn.escom.trejol.proyectocomposemoviles.services

import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.auth.SignUpResponse
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.category.CategoryResult
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.deliveryLocation.DeliveryLocationResult
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

interface DeliveryLocationService {

    @GET("/api/deliveryLocation")
    suspend fun getDeliveryLocation(
        @Header("Authorization") token: String
    ): DeliveryLocationResult

    @FormUrlEncoded
    @POST("/api/deliveryLocation")
    suspend fun addDeliveryLocation(
        @Header("Authorization") token: String,
        @Field("name") alias: String,
        @Field("address") address: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("cp") cp: String,
    ): SignUpResponse

    @FormUrlEncoded
    @PUT("/api/deliveryLocation/{id}")
    suspend fun updateDeliveryLocation(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("cp") cp: String,
        @Path("id") id: Number
    ): SignUpResponse
}

object RetrofitClientDeliveryLocation {

    fun makeRetrofitService(): DeliveryLocationService {
        return Retrofit.Builder()
            .baseUrl(Config.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(DeliveryLocationService::class.java)

    }
}