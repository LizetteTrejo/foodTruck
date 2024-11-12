package mx.ipn.escom.trejol.proyectocomposemoviles.services

import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.auth.AuthResponse
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.auth.SignUpResponse
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.auth.UserResponse
import mx.ipn.escom.trejol.proyectocomposemoviles.services.models.category.CategoryResult
import mx.ipn.escom.trejol.proyectocomposemoviles.utils.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @GET("/api/users")
    suspend fun getUsers(): UserResponse
    @POST("/api/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : AuthResponse

    @POST("/api/signup")
    @FormUrlEncoded
    suspend fun signUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("last_name") last_name: String? = null,
    ) : SignUpResponse

}

object RetrofitClientAuth {

    fun makeRetrofitService(): AuthService {
        return Retrofit.Builder()
            .baseUrl(Config.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(AuthService::class.java)

    }
}