package com.full.recetas.theme.mainNoLogged

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.full.recetas.models.Recipe

class HomeViewModel: ViewModel() {
    private val _searchBarText = MutableLiveData("")
    val searchBarText: LiveData<String> = _searchBarText

    private val _recipes = MutableLiveData<Array<Recipe>>(emptyArray())

    /**
     * Recetas que se muestran en pantalla y en la barra de busqueda
     */
    private val _auxRecipes = MutableLiveData<MutableList<Recipe>>(_recipes.value?.toMutableList() ?: mutableListOf())
    val auxRecipes: LiveData<MutableList<Recipe>> = _auxRecipes

    //This function is called when the component is created
    /*
    init {
        if (API.trendingRecipes.value == null) {
            viewModelScope.launch {
                val result = API.service.getTrendingRecipes()

                if (result.isSuccessful) {
                    val data: DataResponse<Array<Recipe>> = result.body()!!

                    if (data.code == 200) {
                        API.setRecipes(data.data!!)
                    } else {
                        Log.e("MainViewModel", "Error getting trending recipes: ${data.code}")
                    }
                } else {
                    Log.e("MainViewModel", "Error getting trending recipes: ${result.code()}")
                }
            }
        }else{
            _recipes.value = API.trendingRecipes.value
            _auxRecipes.value = _recipes.value?.toMutableList() ?: mutableListOf()
        }
    }
    */

    fun filterRecipes(){}

    fun onSearchBarTextChanged(text: String){
        _searchBarText.value = text
        filterRecipes()
    }
}