package com.full.recetas.network

import com.full.recetas.models.Recipe
import com.full.recetas.models.Tag
import com.full.recetas.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @GET("api/recipe")
    suspend fun getRecipe(@Query("id") id: String): Response<DataResponse<Recipe>>

    @PUT("api/recipe/like")
    suspend fun likeRecipe(@Body like: Map<String, String>): Response<DataResponse<Recipe>>

    @PUT("api/recipe/unlike")
    suspend fun unlikeRecipe(@Body like: Map<String, String>): Response<DataResponse<Recipe>>

    @GET("api/recipes/likes")
    suspend fun getLikes(@Query("id") id: String): Response<DataResponse<Array<Recipe>>>

    @GET("api/tags")
    suspend fun getTags(): Response<DataResponse<Array<Tag>>>

    @POST("api/recipe")
    suspend fun createRecipe(@Body recipe: Recipe, @Query("edit") isEdit: Boolean): Response<DataResponse<String>>

    // Recipes by userID
    @GET("api/recipes/user")
    suspend fun getRecipesByUser(@Query("id") id: String): Response<DataResponse<Array<Recipe>>>

    // Delete recipe by ID
    @POST("api/recipe/delete")
    suspend fun deleteRecipe(@Body recipe: Recipe): Response<DataResponse<String>>

    // Follow user
    @POST("api/user/follow")
    suspend fun followUser(@Body follow: Map<String, String>): Response<DataResponse<String>>

    // Unfollow user
    @POST("api/user/unfollow")
    suspend fun unfollow(@Body follow: Map<String, String>): Response<DataResponse<String>>

    //Get user by id
    @GET("api/user/id")
    suspend fun getUserById(@Query("id") id: String): Response<DataResponse<User>>

    //Register user
    @POST("api/user/register")
    suspend fun registerUser(@Body user: User): Response<DataResponse<String>>
}