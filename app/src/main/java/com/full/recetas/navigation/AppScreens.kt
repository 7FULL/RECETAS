package com.full.recetas.navigation

sealed class AppScreens(val route: String) {
    object Login : AppScreens("login")
    object ForgotPassword : AppScreens("forgotPassword")
    object CheckToken : AppScreens("checkToken")
    object NewPassword : AppScreens("newPassword")
    object HomeNoLogged : AppScreens("mainNoLogged")
    object HomeLogged : AppScreens("mainLogged")
    object Recipe : AppScreens("recipe")
    object Likes : AppScreens("likes")
    object CreateRecipe : AppScreens("createRecipe")
    object Profile : AppScreens("profile")
    object UserProfile : AppScreens("userProfile")
}
