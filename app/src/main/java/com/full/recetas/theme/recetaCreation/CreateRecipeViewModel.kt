package com.full.recetas.theme.recetaCreation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.recetas.navigation.AppScreens
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.network.API
import kotlinx.coroutines.launch

class CreateRecipeViewModel: ViewModel() {

    // Recipe data
    val _recipeName = MutableLiveData<String>()
    val recipeName: LiveData<String> = _recipeName

    val _recipeDescription = MutableLiveData<String>()
    val recipeDescription: LiveData<String> = _recipeDescription

    val _recipeMinutes = MutableLiveData<String>()
    val recipeMinutes: LiveData<String> = _recipeMinutes

    // All tags
    private val _tags = MutableLiveData<List<String>>()
    val tags: LiveData<List<String>> = _tags

    // Tags selected
    private val _selectedTags = MutableLiveData<List<String>>()
    val selectedTags: LiveData<List<String>> = _selectedTags

    init {
        if (!API.isLogged){
            NavigationManager.instance!!.navigate(AppScreens.Login.route)
        }else{
            // Get tags
            getTags()
        }
    }

    // Select tag
    fun selectTag(tag: String){
        val list = _selectedTags.value?.toMutableList() ?: mutableListOf()

        if(list.contains(tag)){
            list.remove(tag)
        }else{
            list.add(tag)
        }

        _selectedTags.value = list
    }

    // Get tags
    private fun getTags(){
        viewModelScope.launch{
            val response = API.service.getTags()

            if(response.isSuccessful){
                val data = response.body()!!

                if(data.code == 200){
                    _tags.value = data.data!!.map { it.name }
                }
            }
        }
    }

    // Create recipe
    fun createRecipe(){
        //TODO
    }

    // Check if the recipe is valid
    fun isValid(): Boolean{
        return recipeName.value!!.isNotEmpty()
                && recipeDescription.value!!.isNotEmpty()
                && recipeMinutes.value!!.isNotEmpty()
    }

    // On change name
    fun onChangeName(name: String){
        _recipeName.value = name
    }

    // On change description
    fun onChangeDescription(description: String){
        _recipeDescription.value = description
    }

    // On change minutes
    fun onChangeMinutes(minutes: String, type: String){
        // If type in hours, convert to minutes
        if (type == "H"){
            _recipeMinutes.value = (minutes.toInt() * 60).toString()
        }

        _recipeMinutes.value = minutes
    }
}