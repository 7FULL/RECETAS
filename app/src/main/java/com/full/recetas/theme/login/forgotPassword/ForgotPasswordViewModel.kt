package com.full.recetas.ui.theme.login.forgotPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.network.API
import kotlinx.coroutines.launch

class ForgotPasswordViewModel: ViewModel() {
    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _enableCheck = MutableLiveData<Boolean>()
    val enableCheck : LiveData<Boolean> = _enableCheck

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    fun checkEmail(email: String) {
        _email.value = email
        _enableCheck.value = email.isNotEmpty() && email.contains("@") && email.contains(".") && email.length > 5
    }

    fun checkPassword(){
        viewModelScope.launch {
            try{
                val response = API.service.forgotPassword(_email.value!!)

                if(response.isSuccessful){
                    if (response.body()!!.code != 200){
                        _error.value = "Error al enviar el correo de recuperación de contraseña"
                    }else{
                        NavigationManager.instance?.navigate("checkToken")
                    }
                }else{
                    _error.value = "Error al enviar el correo de recuperación de contraseña"
                }
            }catch (e: Exception){
                _error.value = "Error al enviar el correo de recuperación de contraseña"
            }
        }
    }
}