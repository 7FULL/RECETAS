package com.full.recetas.navigation

import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.full.recetas.LockScreenOrientation
import com.full.recetas.theme.login.Login
import com.full.recetas.ui.theme.login.LoginViewModel
import com.full.recetas.ui.theme.login.checkToken.CheckToken
import com.full.recetas.ui.theme.login.checkToken.CheckTokenViewModel
import com.full.recetas.ui.theme.login.checkToken.NewPassword
import com.full.recetas.ui.theme.login.checkToken.NewPasswordViewModel
import com.full.recetas.ui.theme.login.forgotPassword.ForgotPassword
import com.full.recetas.ui.theme.login.forgotPassword.ForgotPasswordViewModel
import com.full.recetas.theme.mainNoLogged.Home
import com.full.recetas.theme.mainNoLogged.HomeViewModel
import com.full.recetas.theme.receta.Recipe
import com.full.recetas.theme.receta.RecipeViewModel

object NavigationManager{
    var instance: NavHostController? = null;
    @Composable
    fun InitializeNavigator() {

        if (instance == null) instance = rememberNavController()

        NavHost(navController = instance!!, startDestination = AppScreens.HomeLogged.route+"?id=1")
        {
            composable(AppScreens.Login.route) {
                Login(loginViewModel = LoginViewModel())
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(AppScreens.ForgotPassword.route) {
                ForgotPassword(forgotPasswordViewModel = ForgotPasswordViewModel())
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(AppScreens.CheckToken.route) {
                CheckToken(checkTokenViewModel = CheckTokenViewModel())
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(AppScreens.NewPassword.route) {
                NewPassword(newPasswordViewModel = NewPasswordViewModel())
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(AppScreens.HomeNoLogged.route) {
                Home(vm = HomeViewModel())
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(AppScreens.HomeLogged.route) {
                Home(vm = HomeViewModel(), loggedIn = true)
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(
                    route = "${AppScreens.Recipe.route}?id={id}",
                    arguments = listOf(
                        navArgument("id") { type = NavType.StringType }
                    )
                ) {
                Recipe(productoViewModel = RecipeViewModel(), id = it.arguments?.getString("id") ?: "")
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
        }
    }
}