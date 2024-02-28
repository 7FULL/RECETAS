package com.full.recetas.navigation

import android.content.pm.ActivityInfo
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
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
import com.full.recetas.theme.home.Home
import com.full.recetas.theme.home.HomeViewModel
import com.full.recetas.theme.likes.Likes
import com.full.recetas.theme.likes.LikesViewModel
import com.full.recetas.theme.profile.Profile
import com.full.recetas.theme.profile.ProfileViewModel
import com.full.recetas.theme.receta.Recipe
import com.full.recetas.theme.receta.RecipeViewModel
import com.full.recetas.theme.recetaCreation.CreateRecipeViewModel
import com.full.recetas.theme.userProfile.UserProfile
import com.full.recetas.theme.userProfile.UserProfileViewModel
import com.full.recetas.utils.SavePhotoToGalleryUseCase

object NavigationManager{
    var instance: NavHostController? = null;
    @Composable
    fun InitializeNavigator() {

        if (instance == null) instance = rememberNavController()

        NavHost(navController = instance!!, startDestination = AppScreens.HomeNoLogged.route)
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
                    route = "${AppScreens.Recipe.route}?id={id}&isLiked={isLiked}",
                    arguments = listOf(
                        navArgument("id") { type = NavType.StringType }
                    )
                ) {

                Log.i("Recipe", "Recipe route: ${it.arguments?.getString("id")}")
                Log.i("Recipe", "Recipe route: ${it.arguments?.getBoolean("isLiked")}")

                Recipe(vm = RecipeViewModel(id = it.arguments?.getString("id") ?: ""))
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(AppScreens.Likes.route) {
                Likes(vm = LikesViewModel())
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(
                route = AppScreens.CreateRecipe.route + "?recipe={recipe}",
                arguments = listOf(
                    navArgument("recipe") { type = NavType.StringType }
                )
            ) {
                com.full.recetas.theme.recetaCreation.CreateRecipe(vm = CreateRecipeViewModel(
                    SavePhotoToGalleryUseCase(LocalContext.current),
                    recipeString = it.arguments?.getString("recipe") ?: ""
                ))
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(AppScreens.Profile.route) {
                Profile(vm = ProfileViewModel())
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(
                route = "${AppScreens.UserProfile.route}?id={id}",
                arguments = listOf(
                    navArgument("id") { type = NavType.StringType }
                )
            ) {
                UserProfile(vm = UserProfileViewModel(id = it.arguments?.getString("id") ?: ""))
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
        }
    }
}