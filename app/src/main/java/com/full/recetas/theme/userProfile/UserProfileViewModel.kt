package com.full.recetas.theme.userProfile

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
import kotlinx.coroutines.launch

class UserProfileViewModel(id: String): ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes = _recipes

    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing: LiveData<Boolean> = _isFollowing

    private val _followersCount = MutableLiveData<Int>()
    val followersCount: LiveData<Int> = _followersCount

    init {
        viewModelScope.launch {
            // We obtain the user from the API
            val response = API.service.getUserById(id)

            if(response.isSuccessful){
                val data = response.body()!!

                if(data.code == 200){
                    _user.value = data.data

                    getRecipes(data.data!!._id)

                    if(API.isLogged){
                        val currentUser = API.User.value!!

                        _isFollowing.value = currentUser.following.contains(data.data!!._id)

                        _followersCount.value = data.data!!.followers.size
                    }
                }else{
                    NavigationManager.instance!!.navigate(AppScreens.HomeLogged.route)

                    // We show an error message
                    Toast.makeText(API.mainActivity, "Error obteniendo los datos del usuario", Toast.LENGTH_SHORT).show()
                }
            }else{
                NavigationManager.instance!!.navigate(AppScreens.HomeLogged.route)

                // We show an error message
                Toast.makeText(API.mainActivity, "Error obteniendo los datos del usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun follow(){
        val map = mapOf("userToFollow" to user.value?._id!!, "userFollowing" to API.User.value!!._id)

        viewModelScope.launch {
            val response = API.service.followUser(map)

            if (response.isSuccessful) {
                val data = response.body()!!

                if (data.code == 200) {
                    val currentUser = API.User.value!!
                    currentUser.following.add(user.value?._id!!)
                    API.setUser(currentUser)

                    _user.value!!.followers.add(currentUser._id)

                    _followersCount.value = _followersCount.value?.plus(1)

                    _isFollowing.value = true
                } else {
                    Toast.makeText(
                        API.mainActivity,
                        "Error siguiendo al usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    API.mainActivity,
                    "Error siguiendo al usuario",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun unfollow(){
        val map = mapOf("userToUnfollow" to user.value?._id!!, "userUnfollowing" to API.User.value!!._id)

        viewModelScope.launch {
            val response = API.service.unfollow(map)

            if (response.isSuccessful) {
                val data = response.body()!!

                if (data.code == 200) {
                    val currentUser = API.User.value!!
                    currentUser.following.add(user.value?._id!!)
                    API.setUser(currentUser)

                    _user.value!!.followers.remove(currentUser._id)

                    _followersCount.value = _followersCount.value?.minus(1)

                    _isFollowing.value = false
                } else {
                    Toast.makeText(
                        API.mainActivity,
                        "Error dejando de seguir al usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    API.mainActivity,
                    "Error dejando de seguir al usuario",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun getRecipes(id: String){
        val response = API.service.getRecipesByUser(id)

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