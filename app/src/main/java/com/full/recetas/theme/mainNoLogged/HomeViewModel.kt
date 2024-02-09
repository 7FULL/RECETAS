package com.full.recetas.theme.mainNoLogged

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

    //This function is called when the component is created
    init {
        if (API.trendingRecipes.value == null) {
            viewModelScope.launch {
                val result = API.service.getTrendingRecipes()

                if (result.isSuccessful) {
                    val data: DataResponse<Array<Recipe>> = result.body()!!

                    if (data.code == 200) {
                        API.setTrendingRecipes(data.data!!)

                        _trendingRecipes.value = data.data
                    } else {
                        Log.e("MainViewModel", "Error getting trending recipes: ${data.code}")
                    }
                } else {
                    Log.e("MainViewModel", "Error getting trending recipes: ${result.code()}")
                }
            }
        }else{
            _trendingRecipes.value = API.trendingRecipes.value
        }

        if (API.allRecipes.value == null) {
            viewModelScope.launch {
                val result = API.service.getRecipes()

                if (result.isSuccessful) {
                    val data: DataResponse<Array<Recipe>> = result.body()!!

                    if (data.code == 200) {
                        API.setAllRecipes(data.data!!)

                        _recipes.value = data.data
                        _auxRecipes.value = data.data!!.toMutableList()
                    } else {
                        Log.e("MainViewModel", "Error getting trending recipes: ${data.code}")
                    }
                } else {
                    Log.e("MainViewModel", "Error getting trending recipes: ${result.code()}")
                }
            }
        }else{
            _recipes.value = API.allRecipes.value
            _auxRecipes.value = API.allRecipes.value!!.toMutableList()
        }
    }

    fun filterRecipes(){}

    fun onSearchBarTextChanged(text: String){
        _searchBarText.value = text
        filterRecipes()
    }
}