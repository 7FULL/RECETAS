package com.full.recetas.network

import com.full.recetas.models.Recipe
import com.full.recetas.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    /**
     * Tipical function to check if the api is up
     * @return It returns "PON"
     */
    @GET("api/pin")
    suspend fun pin(): Response<DataResponse<String>>

    @GET("api/recipes/trending")
    suspend fun getTrendingRecipes(): Response<DataResponse<Array<Recipe>>>

    @GET("api/recipes")
    suspend fun getRecipes(): Response<DataResponse<Array<Recipe>>>

    @GET("api/user")
    suspend fun getUser(@Query("email") email: String): Response<DataResponse<User>>

    @POST("api/user/login")
    suspend fun login(@Body credentials: Map<String, String> ): Response<DataResponse<User>>

    @POST("api/user/checkToken")
    suspend fun checkToken(@Body token: String): Response<DataResponse<String>>

    @POST("api/user/newPassword")
    suspend fun newPassword(@Body newPassword: Map<String, String>): Response<DataResponse<String>>

    @POST("api/user/forgotPassword")
    suspend fun forgotPassword(@Body email: String): Response<DataResponse<String>>
}