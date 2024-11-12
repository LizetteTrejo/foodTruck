package mx.ipn.escom.trejol.proyectocomposemoviles.services

import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.auth.SignUpResponse
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.category.CategoryResult
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.products.ProductsResponse
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

interface ProductService {

    @GET("/api/product")
    suspend fun getProducts(
        @Query("id_category") product: Number? = null
    ): ProductsResponse

    @FormUrlEncoded
    @POST("/api/product")
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("price") price: Number,
        @Field("category_id") id_category: Number,
    ): SignUpResponse

    @FormUrlEncoded
    @PUT("/api/product/{id}")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("price") price: Number,
        @Field("category_id") id_category: Number,
        @Path("id") id: Number
    ): SignUpResponse

    @PUT("/api/product/changeStatus/{id}")
    suspend fun changeStatusProduct(
        @Header("Authorization") token: String,
        @Path("id") id: Number
    ): SignUpResponse

}

object RetrofitClientProduct {

    fun makeRetrofitService(): ProductService {
        return Retrofit.Builder()
            .baseUrl(Config.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ProductService::class.java)

    }
}