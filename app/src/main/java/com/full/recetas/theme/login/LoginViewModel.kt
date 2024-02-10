package com.full.recetas.ui.theme.login

import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.recetas.models.User
import com.full.recetas.navigation.AppScreens
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.network.API
import com.full.recetas.network.DataResponse
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val auth = Firebase.auth

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading : LiveData<Boolean> = _isLoading

    private val _enableLogin = MutableLiveData<Boolean>()
    val enableLogin : LiveData<Boolean> = _enableLogin

    private val _checkRememberMe = MutableLiveData<Boolean>(false)
    val checkRememberMe : LiveData<Boolean> = _checkRememberMe

    /*
    init {
        //TODO: Podria hacerse que tuviese que confirmase el inicio de sesion con huella dactilar
        if(!API.isLogged){
            //Si el usuario tiene guardado el email y el password en las shared preferences lo cargamos
            val sharedPreferences = API.mainActivity!!.getSharedPreferences("login", MODE_PRIVATE)
            val email = sharedPreferences.getString("username", "")
            val password = sharedPreferences.getString("password", "")

            if (email != null && password != null && email.isNotEmpty() && password.isNotEmpty()) {
                _username.value = email
                _password.value = password
                _checkRememberMe.value = true

                viewModelScope.launch {
                    var result = API.service.getUser(email!!)

                    if (result.isSuccessful) {
                        val data: DataResponse<User> = result.body()!!

                        if (data.code == 200) {
                            API.setUser(data.data!!)
                            NavigationManager.instance?.navigate(AppScreens.HomeLogged.route)
                        }
                        else if (data.code == 401) {
                            _error.value = "Usuario no reconocido"
                        }
                    } else {
                        Log.i("CRM", "Error en el login con google")
                        Log.i("CRM", result.toString())
                    }
                }
            }
        }
    }*/

    fun signInWithGoogle(credential: AuthCredential) {
        viewModelScope.launch {
            try{
                auth.signInWithCredential(credential).addOnCompleteListener {
                    task-> if(task.isSuccessful) {
                        Log.i("CRM", "Login correcto con google")

                        val email = task.result.user?.email

                    if (email != null) {
                        Log.i("CRM", email)

                        viewModelScope.launch {
                            var result = API.service.getUser(email!!)

                            if (result.isSuccessful) {
                                val data: DataResponse<User> = result.body()!!

                                if (data.code == 200) {
                                    API.setUser(data.data!!)
                                    NavigationManager.instance?.navigate(AppScreens.HomeLogged.route)
                                }
                                else if (data.code == 401) {
                                     _error.value = "Usuario no reconocido"
                                }
                            } else {
                                Log.i("CRM", "Error en el login con google")
                                Log.i("CRM", result.toString())
                            }
                        }
                    } else{
                        Log.i("CRM", "Error en el login con google")
                        Log.i("CRM", "El email es nulo")
                    }
                    } else {
                        Log.i("CRM", "Error en el login con google")
                        Log.i("CRM", task.exception.toString())
                    }
                }.addOnFailureListener {
                    Log.i("CRM", "Error en el login con google")
                    Log.i("CRM", it.toString())
                }
            }
            catch (e: Exception) {
                Log.i("CRM", "Error en el login con google")
                Log.i("CRM", e.toString())
            }
        }
    }

    fun onLoginChanged(username: String, password: String) {
        _username.value = username
        if (password.length < 11) {
            _password.value = password
        }

        _enableLogin.value = username.isNotEmpty() && password.isNotEmpty() && password.length >= 8 && !_isLoading.value!!
    }

    fun onErrorChanged(error: String){
        _error.value = error
    }

    fun onRememberMeChanged(rememberMe: Boolean) {
        _checkRememberMe.value = rememberMe

        if (!rememberMe) {
            API.mainActivity!!.getSharedPreferences("login", MODE_PRIVATE).edit().apply {
                putString("username", "")
                putString("password", "")
                apply()
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            _isLoading.value = true

            //We generate a map with the username and the password
            val credentials = mapOf("username" to _username.value!!, "password" to _password.value!!)

            try{
                var result = API.service.login(credentials)

                if (result.isSuccessful) {

                    val response: DataResponse<User> = result.body()!!

                    if (response.code == 200) {
                        API.setUser(response.data!!)

                        Log.i("CRM", "Login correcto")
                        Log.i("CRM", response.data.toString())

                        //Si el login ha sido exitoso miramos si el usuario ha checkeado el remember me
                        if (_checkRememberMe.value!!) {
                            //Si lo ha checkeado guardamos el email y el password en las shared preferences
                            API.mainActivity!!.getSharedPreferences("login", MODE_PRIVATE).edit().apply {
                                putString("username", username.value)
                                putString("password", password.value)
                                apply()
                            }
                        }

                        NavigationManager.instance?.navigate(AppScreens.HomeLogged.route)
                    } else {
                        //Podria manejarse tambien los mensajes en el backend y mostrarlos aqui
                        when (response.code) {
                            401 -> {
                                _error.value = "Usuario o contraseña incorrectos"
                            }
                            500 -> {
                                _error.value = "Error en el servidor"
                            }
                            else -> {
                                _error.value = "Error desconocido"
                            }
                        }

                        //Limpiamos el password y email para usabilidad
                        _password.value = ""
                        _username.value = ""
                    }

                } else {
                    Log.i("CRM", "Error en el login")
                    Log.i("CRM", result.toString())
                }
            }
            catch (e: Exception) {
                Log.i("CRM", "Error en el login")
                Log.i("CRM", e.toString())
                _error.value = "Lo sentimos nuestros servidores no estan disponibles en estos momentos"
            }

            _isLoading.value = false
        }
    }
}