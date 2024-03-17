package com.full.recetas.theme.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.recetas.models.User
import com.full.recetas.navigation.AppScreens
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.network.API
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword : LiveData<String> = _confirmPassword

    private val _name = MutableLiveData<String>()
    val name : LiveData<String> = _name

    private val _surname = MutableLiveData<String>()
    val surname : LiveData<String> = _surname

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading : LiveData<Boolean> = _isLoading

    private val _enableLogin = MutableLiveData<Boolean>()
    val enableLogin : LiveData<Boolean> = _enableLogin

    fun onRegisterChanged(username: String, password: String, name: String, surname: String, email: String, confirmPassword: String) {
        _username.value = username
        if (password.length < 11) {
            _password.value = password
        }

        _name.value = name
        _surname.value = surname
        _email.value = email
        _confirmPassword.value = confirmPassword

        _enableLogin.value = username.isNotEmpty() && password.isNotEmpty() && password.length >= 8 && !_isLoading.value!! && name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty() && password == _confirmPassword.value
    }

    fun register() {
        val client = User(name = _name.value!!, surname = _surname.value!!, image = "https://cdn.icon-icons.com/icons2/1508/PNG/512/systemusers_104569.png"  ,email = _email.value!!, username = _username.value!!, password = _password.value!!)

        viewModelScope.launch {
            _isLoading.value = true
            val result = API.service.registerUser(client)
            _isLoading.value = false

            if (result.isSuccessful) {
                if (result.body()!!.code == 200) {
                    _error.value = "Usuario registrado correctamente"

                    //TODO: Tenemos que hacer que devuelva la ip el back para que podamos hacer el login

                    //API.setUser(client)

                    //NavigationManager.instance!!.navigate(AppScreens.HomeLogged.route)

                    NavigationManager.instance!!.navigate(AppScreens.Login.route)
                }
            } else {
                _error.value = "Error al registrar el usuario"
            }
        }
    }
}