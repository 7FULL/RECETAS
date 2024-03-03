package com.full.recetas.theme.receta

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.recetas.R
import com.full.recetas.models.Ingredient
import com.full.recetas.models.Recipe
import com.full.recetas.models.User
import com.full.recetas.network.API
import com.full.recetas.network.DataResponse
import kotlinx.coroutines.launch

class RecipeViewModel(id: String): ViewModel(){

    private val recipe = MutableLiveData<Recipe>()
    val recipeData: LiveData<Recipe> = recipe

    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean> = _isLiked

    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing: LiveData<Boolean> = _isFollowing

    private val _likesText = MutableLiveData<String>()
    val likesText: LiveData<String> = _likesText

    init {
        getrecipe(id, true)
    }

    fun getrecipe(id: String, initial : Boolean = false){
        viewModelScope.launch {
            val response = API.service.getRecipe(id)

            if(response.isSuccessful){
                val data: DataResponse<Recipe> = response.body()!!

                if(data.code == 200){
                    recipe.value = data.data

                    if(API.isLogged){
                        val currentUser = API.User.value!!

                        if (initial) {
                            _isLiked.value = currentUser.likes.contains(data.data!!._id)

                            _isFollowing.value = currentUser.following.contains(data.data!!.publisher._id)

                            val likes = recipe.value!!.likes

                            _likesText.value = if (likes > 1000) "${likes / 1000}K" else likes.toString()
                        }
                    }
                }else{
                    Log.i("RecipeViewModel", "Error getting recipe: $data")

                    recipe.value = Recipe(
                        _id = "0",
                        name = "Error",
                        description = "Error getting recipe",
                        ingredients = listOf(Ingredient("Error", "0")),
                        cookingInstructions = listOf("Error"),
                        image = "https://cdn-icons-png.flaticon.com/512/5220/5220262.png",
                        likes = 0,
                        minutes = 0,
                        rating = 0,
                        tags = listOf("Error"),
                        publisher = User(
                            _id = "0",
                            name = "Error",
                            phone = "Error",
                            surname = "Error",
                            email = "Error",
                            password = "Error",
                            token = "Error",
                            username = "Error",
                            image = "Error",
                            recipes = listOf(),
                            following = arrayListOf(),
                            followers = arrayListOf(),
                        )
                    )
                }
            }else{
                Log.i("RecipeViewModel", "Error getting recipe: ${response.code()} ${response.message()}")
            }
        }
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

                    _isLiked.value = true

                    //We update our user's liked recipes
                    API.User.value!!.likes.add(id)

                    recipe.value!!.likes++

                    val likes = recipe.value!!.likes

                    _likesText.value = if (likes > 1000) "${likes / 1000}K" else likes.toString()
                }else{
                    Log.i("RecipeViewModel", "Error liking recipe: $data")
                }
            }else{
                Log.i("RecipeViewModel", "Error liking recipe: ${response.code()} ${response.message()}")
            }
        }
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

                    recipe.value!!.likes--

                    _isLiked.value = false

                    //We update our user's liked recipes
                    API.User.value!!.likes.remove(id)

                    val likes = recipe.value!!.likes

                    _likesText.value = if (likes > 1000) "${likes / 1000}K" else likes.toString()
                }else{
                    Log.i("RecipeViewModel", "Error unliking recipe: $data")
                }
            }else{
                Log.i("RecipeViewModel", "Error unliking recipe: ${response.code()} ${response.message()}")
            }
        }
    }

    fun followUser(id: String){
        val currentUser = API.User.value!!

        val map = mapOf("userToFollow" to id, "userFollowing" to currentUser._id)

        viewModelScope.launch {
            val response = API.service.followUser(map)

            if(response.isSuccessful){
                val data: DataResponse<String> = response.body()!!

                if(data.code == 200){
                    Log.i("RecipeViewModel", "Followed user: $data")

                    recipe.value!!.publisher.followers.add(currentUser._id)

                    _isFollowing.value = true
                }else{
                    Log.i("RecipeViewModel", "Error following user: $data")
                }
            }else{
                Log.i("RecipeViewModel", "Error following user: ${response.code()} ${response.message()}")
            }
        }
    }

    fun unfollowUser(id: String){
        val currentUser = API.User.value!!

        val map = mapOf("userToUnfollow" to id, "userUnfollowing" to currentUser._id)

        viewModelScope.launch {
            val response = API.service.unfollow(map)

            if(response.isSuccessful){
                val data: DataResponse<String> = response.body()!!

                if(data.code == 200){
                    Log.i("RecipeViewModel", "Unfollowed user: $data")

                    recipe.value!!.publisher.followers.remove(currentUser._id)

                    _isFollowing.value = false
                }else{
                    Log.i("RecipeViewModel", "Error unfollowing user: $data")
                }
            }else{
                Log.i("RecipeViewModel", "Error unfollowing user: ${response.code()} ${response.message()}")
            }
        }
    }
}

/*

Recipe("1", "Macarrones con chorizo",
            User("1", "JuanManolo24", "", "Juan", "Manolo", "email@gmail.com", "678 04 15 77", R.drawable.logogoogle_removebg.toString(), listOf(), listOf(), listOf(), ""),
            R.drawable.receta_default.toString(),
            4,
            30,
            "Esto es una descirpcion",
            listOf("Paso 1", "Paso 2", "Paso 3"),
            listOf("Macarrones", "Chorizo", "Tomate", "Cebolla", "Pimiento", "Aceite", "Sal", "Pimienta"),
            5324,
            listOf("Pasta", "Cena", "Horno"),
        )

*/