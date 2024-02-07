package com.full.recetas.ui.theme.login.checkToken

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.network.API
import kotlinx.coroutines.launch

class NewPasswordViewModel: ViewModel() {
    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _enableCheck = MutableLiveData<Boolean>()
    val enableCheck : LiveData<Boolean> = _enableCheck

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _password2 = MutableLiveData<String>()
    val password2 : LiveData<String> = _password2

    fun checkPassword(password: String, password2: String) {
        _password.value = password
        _password2.value = password2
        _enableCheck.value = password.isNotEmpty() && password2.isNotEmpty() && password == password2
    }

    fun checkPassword(){
        viewModelScope.launch {
            try{
                val body = mapOf(
                    "token" to API.token,
                    "password" to _password.value!!
                )
                val response = API.service.newPassword(body as Map<String, String>)

                if(response.isSuccessful){
                    if (response.body()!!.code != 200){
                        _error.value = "Error al comprobar el token"
                    }else{
                        NavigationManager.instance?.navigate("login")
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