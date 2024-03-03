package com.full.recetas.theme.likes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.recetas.models.Recipe
import com.full.recetas.navigation.AppScreens
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.network.API
import kotlinx.coroutines.launch

class LikesViewModel: ViewModel() {

    private val _likes = MutableLiveData<Array<Recipe>>(emptyArray())

    private val _searchBarText = MutableLiveData("")
    val searchBarText: LiveData<String> = _searchBarText

    private val _auxLikes = MutableLiveData<MutableList<Recipe>>(_likes.value?.toMutableList() ?: mutableListOf())
    val auxLikes: LiveData<MutableList<Recipe>> = _auxLikes

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        if (!API.isLogged){
            NavigationManager.instance!!.navigate(AppScreens.Login.route)
        }else{
            //We get the user's likes
            viewModelScope.launch {
                val user = API.User.value!!

                val result = API.service.getLikes(user.email)

                if(result.isSuccessful){
                    val data = result.body()!!

                    if(data.code == 200){
                        _likes.value = data.data!!

                        _auxLikes.value = _likes.value?.toMutableList() ?: mutableListOf()

                        _isLoading.value = false
                    }else{
                        Log.e("LikesViewModel", "Error getting likes: ${data.code}")
                    }
                }else{
                    Log.e("LikesViewModel", "Error getting likes: ${result.code()}")
                }
            }
        }
    }

    fun onSearchBarTextChanged(text: String){
        _searchBarText.value = text

        filterRecipes()
    }

    fun filterRecipes(){
        val text = _searchBarText.value!!

        _auxLikes.value = _likes.value?.filter {
            it.name.contains(text, true)
        }?.toMutableList() ?: mutableListOf()
    }
}