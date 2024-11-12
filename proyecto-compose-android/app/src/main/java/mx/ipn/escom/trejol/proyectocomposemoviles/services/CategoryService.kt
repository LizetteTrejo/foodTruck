package mx.ipn.escom.trejol.proyectocomposemoviles.services

import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.auth.SignUpResponse
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.category.CategoryResult
import mx.ipn.escom.trejol.proyectocomposemoviles.utils.Config
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryService {

    @GET("/api/category")
    suspend fun getCategories(): CategoryResult

    @FormUrlEncoded
    @POST("/api/category")
    suspend fun addCategory(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("description") description: String,
    ): SignUpResponse

    @FormUrlEncoded
    @PUT("/api/category/{id}")
    suspend fun updateCategory(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("description") description: String,
        @Path("id") id: Number
    ): SignUpResponse

    @PUT("/api/category/changeStatus/{id}")
    suspend fun changeStatusCategory(
        @Header("Authorization") token: String,
        @Path("id") id: Number
    ): SignUpResponse

}

object RetrofitClientCategory {

    fun makeRetrofitService(): CategoryService {
        return Retrofit.Builder()
            .baseUrl(Config.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(CategoryService::class.java)

    }
}