package com.full.recetas.theme.profile

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.recetas.models.Recipe
import com.full.recetas.models.User
import com.full.recetas.navigation.AppScreens
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.network.API
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    val storage = Firebase.storage.reference

    init {
        if (!API.isLogged){
            NavigationManager.instance!!.navigate(AppScreens.Login.route)
        }else{
            _user.value = API.User.value!!

            viewModelScope.launch {
                getRecipes()
            }
        }
    }

    fun deleteRecipe(recipe: Recipe){
        viewModelScope.launch {
            val response = API.service.deleteRecipe(recipe)

            if(response.isSuccessful){
                val data = response.body()!!

                if(data.code == 200){

                    val imageRef = storage.child("images/${recipe._id}.png")
                    imageRef.delete().addOnSuccessListener {
                        _recipes.value = _recipes.value!!.filter { it._id != recipe._id }
                    }.addOnFailureListener {
                        Toast.makeText(API.mainActivity, "Error eliminando la receta", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(API.mainActivity, "Error eliminando la receta", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(API.mainActivity, "Error eliminando la receta", Toast.LENGTH_SHORT).show()

                Log.i("ProfileViewModel", "Error deleting recipe: ${response.code()}")
            }
        }
    }

    fun editRecipe(recipe: Recipe){
        val jsonString = API.gson.toJson(recipe)

        NavigationManager.instance!!.navigate(AppScreens.CreateRecipe.route+"?recipe=${jsonString}")
    }

    private suspend fun getRecipes(){
        val response = API.service.getRecipesByUser(_user.value!!._id)

        if(response.isSuccessful){
            val data = response.body()!!

            if(data.code == 200){
                _recipes.value = data.data!!.toList()
            }else{
                _recipes.value = listOf()

                Toast.makeText(API.mainActivity, "Error obteniendo tus recetas", Toast.LENGTH_SHORT).show()
            }
        }else{
            _recipes.value = listOf()

            Toast.makeText(API.mainActivity, "Error obteniendo tus recetas", Toast.LENGTH_SHORT).show()
        }
    }
}