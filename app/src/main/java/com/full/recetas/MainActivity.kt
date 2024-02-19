package com.full.recetas

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SupervisedUserCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.full.recetas.navigation.AppScreens
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.network.API
import com.full.recetas.ui.theme.CRMTheme


class MainActivity : ComponentActivity() {

    private lateinit var requestMultiplePermission: ActivityResultLauncher<Array<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        API.mainActivity = this

        requestMultiplePermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){
            var isGranted = false
            it.forEach { s, b ->
                isGranted = b

                if (!isGranted){
                    //Toast.makeText(this, "Permiso: ${s} no concedido", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setContent {
            CRMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    requestMultiplePermission.launch(
                        arrayOf(
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.INTERNET,
                            android.Manifest.permission.POST_NOTIFICATIONS,
                        )
                    )

                    NavigationManager.InitializeNavigator()

                    //Kalendar(currentDay = LocalDate(1,1,1), kalendarType = KalendarType.Oceanic)
                }
            }
        }
    }
}


@Composable
fun BottomBar(modifier: Modifier = Modifier, selectedIcon: Int) {
    val context = LocalContext.current

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(height = 75.dp)
                .background(color = Color(context.getColor(R.color.primary)))
        )
        Row(modifier.fillMaxWidth()){
            Column (
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ){
                Icon(
                    tint = if (selectedIcon == 0) Color(context.getColor(R.color.selected)) else Color.Black,
                    imageVector = Icons.Rounded.SupervisedUserCircle,
                    contentDescription = "Vector",
                    modifier = Modifier
                        .size(size = 50.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .clickable { if (selectedIcon != 1) NavigationManager.instance?.navigate("") }
                )
            }
            Column (
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ){
                Icon(
                    tint = if (selectedIcon == 1) Color(context.getColor(R.color.selected)) else Color.Black,
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Vector",
                    modifier = Modifier
                        .size(size = 50.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .clickable { if (selectedIcon != 1) NavigationManager.instance?.navigate(AppScreens.Likes.route) }
                )
            }
            Column (
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ){
                Icon(
                    tint = if (selectedIcon == 2) Color(context.getColor(R.color.selected)) else Color.Black,
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "Vector",
                    modifier = Modifier
                        .size(size = 50.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .clickable { if (selectedIcon != 2) NavigationManager.instance?.navigate(AppScreens.CreateRecipe.route) }
                )
            }
            Column (
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ){
                Icon(
                    tint = if (selectedIcon == 3) Color(context.getColor(R.color.selected)) else Color.Black,
                    imageVector = Icons.Rounded.Info,
                    contentDescription = "Vector",
                    modifier = Modifier
                        .size(size = 50.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .clickable { if (selectedIcon != 3) API.logout() }
                )
            }
            Column (
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ){
                Icon(
                    tint = if (selectedIcon == 4) Color(context.getColor(R.color.selected)) else Color.Black,
                    imageVector = Icons.Rounded.CalendarMonth,
                    contentDescription = "Vector",
                    modifier = Modifier
                        .size(size = 50.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .clickable { if (selectedIcon != 4) API.logout() }
                )
            }
        }
    }
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(orientation) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Preview()
@Composable
private fun PruebaPreview() {
    MaterialTheme {
        //Agenda()
        //Login(loginViewModel = LoginViewModel())
    }
}