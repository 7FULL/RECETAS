package com.full.recetas.navigation

sealed class AppScreens(val route: String) {
    object Login : AppScreens("login")
    object ForgotPassword : AppScreens("forgotPassword")
    object CheckToken : AppScreens("checkToken")
    object NewPassword : AppScreens("newPassword")
    object HomeNoLogged : AppScreens("mainNoLogged")
    object HomeLogged : AppScreens("mainNoLogged")
    object Recipe : AppScreens("recipe")
}
