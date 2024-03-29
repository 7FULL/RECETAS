package com.full.recetas.network

import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.full.recetas.MainActivity
import com.full.recetas.models.Recipe
import com.full.recetas.models.User
import com.full.recetas.navigation.NavigationManager
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//https://developer.android.com/studio/run/emulator-networking
//Tenemos que poner 10.0.2.2 en vez de localhost porque estamos en un emulador

//private val BASE_URL = "http://10.0.2.2:3000/"
private val BASE_URL = "https://recetas-backend.onrender.com/"

private val httpClient = OkHttpClient.Builder().addInterceptor(
    BasicAuthInterceptor("user", "user")).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(httpClient)
    .build()

// Lo hacemos singleton porque en uno de los codlabs de Google lo recomiendan porque el create es costoso
// Singleton (usamos lazy para que se cree cuando se necesite y no antes)
object API {
    val service : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create()

    private val _trendingRecipes = MutableLiveData<Array<Recipe>>()
    val trendingRecipes: LiveData<Array<Recipe>> = _trendingRecipes

    private val _allRecipes = MutableLiveData<Array<Recipe>>()
    val allRecipes: LiveData<Array<Recipe>> = _allRecipes

    var mainActivity: MainActivity? = null

    val isLogged: Boolean
        get() = _user.value != null

    private val _user: MutableLiveData<User> = MutableLiveData<User>()
    val User: LiveData<User> = _user

    fun setUser(employee: User) {
        _user.value = employee
    }

    fun setTrendingRecipes(recipes: Array<Recipe>) {
        _trendingRecipes.value = recipes
    }

    fun setAllRecipes(recipes: Array<Recipe>) {
        _allRecipes.value = recipes
    }

    fun logout() {
        NavigationManager.instance?.navigate("login")

        //Limpiamos los shared preferences
        val sharedPreferences = mainActivity!!.getSharedPreferences("login", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        /*
        //Espremoas 1 segundo para que de tiempo a que se cambie de pantalla
        Thread.sleep(1000)
        _user.value = null
        */
    }

    var token = ""
}