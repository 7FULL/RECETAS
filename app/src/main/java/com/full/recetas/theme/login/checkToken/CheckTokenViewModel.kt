package com.full.recetas.ui.theme.login.checkToken

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.network.API
import kotlinx.coroutines.launch

class CheckTokenViewModel: ViewModel() {
    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _enableCheck = MutableLiveData<Boolean>()
    val enableCheck : LiveData<Boolean> = _enableCheck

    private val _token = MutableLiveData<String>()
    val token : LiveData<String> = _token

    fun checkEmail(email: String) {
        _token.value = email
        _enableCheck.value = email.isNotEmpty() && email.length > 6
    }

    fun checkPassword(){
        viewModelScope.launch {
            try{
                val response = API.service.checkToken(_token.value!!)

                if(response.isSuccessful){
                    if (response.body()!!.code != 200){
                        _error.value = "Error al comprobar el token"
                    }else{
                        API.token = _token.value!!
                        NavigationManager.instance?.navigate("newPassword")
                    }
                }else{
                    _error.value = "Error al comprobar el token"
                }
            }catch (e: Exception){
                _error.value = "Error al comprobar el token"
            }
        }
    }
}