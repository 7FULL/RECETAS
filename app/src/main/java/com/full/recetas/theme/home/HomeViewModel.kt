package com.full.recetas.theme.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.recetas.models.Recipe
import com.full.recetas.network.API
import com.full.recetas.network.DataResponse
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _searchBarText = MutableLiveData("")
    val searchBarText: LiveData<String> = _searchBarText

    private val _recipes = MutableLiveData<Array<Recipe>>(emptyArray())

    /**
     * Recetas que se muestran en pantalla y en la barra de busqueda
     */
    private val _auxRecipes = MutableLiveData<MutableList<Recipe>>(_recipes.value?.toMutableList() ?: mutableListOf())
    val auxRecipes: LiveData<MutableList<Recipe>> = _auxRecipes

    private val _trendingRecipes = MutableLiveData<Array<Recipe>>()
    val trendingRecipes: LiveData<Array<Recipe>> = _trendingRecipes

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    //This function is called when the component is created
    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            val result = API.service.getTrendingRecipes()

            if (result.isSuccessful) {
                val data: DataResponse<Array<Recipe>> = result.body()!!

                if (data.code == 200) {
                    API.setTrendingRecipes(data.data!!)

                    _trendingRecipes.value = data.data
                } else {
                    Log.e("HomeViewModel", "Error getting trending recipes: ${data.code}")
                }
            } else {
                Log.e("HomeViewModel", "Error getting trending recipes: ${result.code()}")
            }
        }

        viewModelScope.launch {
            val result = API.service.getRecipes()

            if (result.isSuccessful) {
                val data: DataResponse<Array<Recipe>> = result.body()!!

                if (data.code == 200) {
                    API.setAllRecipes(data.data!!)

                    _recipes.value = data.data
                    _auxRecipes.value = data.data!!.toMutableList()

                    _isLoading.value = false
                } else {
                    Log.e("HomeViewModel", "Error getting trending recipes: ${data.code}")
                }
            } else {
                Log.e("HomeViewModel", "Error getting trending recipes: ${result.code()}")
            }
        }
    }

    fun filterRecipes(){}

    fun onSearchBarTextChanged(text: String){
        _searchBarText.value = text
        filterRecipes()
    }

    fun likeRecipe(id: String){
        viewModelScope.launch {
            val currentUser = API.User.value!!

            val map = mapOf(
                "recipeId" to id,
                "userId" to currentUser._id
            )

            val response = API.service.likeRecipe(map)

            if(response.isSuccessful){
                val data: DataResponse<Recipe> = response.body()!!

                if(data.code == 200){
                    Log.i("RecipeViewModel", "Liked recipe: $data")
                }else{
                    Log.i("RecipeViewModel", "Error liking recipe: $data")
                }
            }else{
                Log.i("RecipeViewModel", "Error liking recipe: ${response.code()} ${response.message()}")
            }
        }

        //We update our user's liked recipes
        API.User.value!!.likes.add(id)

        load()
    }

    fun unlikeRecipe(id: String){
        val currentUser = API.User.value!!

        val map = mapOf(
            "recipeId" to id,
            "userId" to currentUser._id
        )

        viewModelScope.launch {
            val response = API.service.unlikeRecipe(map)

            if(response.isSuccessful){
                val data: DataResponse<Recipe> = response.body()!!

                if(data.code == 200){
                    Log.i("RecipeViewModel", "Unliked recipe: $data")
                }else{
                    Log.i("RecipeViewModel", "Error unliking recipe: $data")
                }
            }else{
                Log.i("RecipeViewModel", "Error unliking recipe: ${response.code()} ${response.message()}")
            }
        }

        //We update our user's liked recipes
        API.User.value!!.likes.remove(id)

        load()
    }
}