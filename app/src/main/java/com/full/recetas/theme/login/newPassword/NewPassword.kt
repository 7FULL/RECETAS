package com.full.recetas.ui.theme.login.checkToken

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.full.recetas.R
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.theme.login.Input

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewPassword(modifier: Modifier = Modifier, newPasswordViewModel: NewPasswordViewModel) {
    val error: String by newPasswordViewModel.error.observeAsState(initial = "")
    val enableCheck: Boolean by newPasswordViewModel.enableCheck.observeAsState(initial = false)
    val password: String by newPasswordViewModel.password.observeAsState(initial = "")
    val password2: String by newPasswordViewModel.password2.observeAsState(initial = "")

    //Pillamos el teclado simplemente para poder ocultarlo
    val keyboard = LocalSoftwareKeyboardController.current

    val context = LocalContext.current

    Box(
        modifier = modifier
            .requiredWidth(width = 360.dp)
            .requiredHeight(height = 800.dp)
            .background(color = Color.White)
    ) {
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(y = (-100).dp)
                .requiredWidth(width = 427.dp)
                .requiredHeight(height = 450.dp)
                .clip(shape = RoundedCornerShape(71.dp))
                .background(
                    brush = Brush.linearGradient(
                        0f to Color(context.getColor(R.color.selected)),
                        1f to Color(context.getColor(R.color.secondary)),
                        start = Offset(554f, -124.5f),
                        end = Offset(32.5f, 385f)
                    )
                ))
        ElevatedCard(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .offset(
                    x = 0.5.dp,
                    y = (-90.5).dp
                )
                .requiredWidth(width = 329.dp)
                .requiredHeight(height = 323.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEFEFE)),
            elevation = CardDefaults.cardElevation(defaultElevation = 50.dp)
        ){}
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 129.dp,
                    y = 91.dp
                )
                .requiredSize(size = 103.dp)
        ) {
            ElevatedCard(
                modifier = Modifier
                    .requiredSize(size = 103.dp),
                shape = RoundedCornerShape(35.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEFEFE)),
                elevation = CardDefaults.cardElevation(defaultElevation = 50.dp)
            ){
                Box(Modifier.fillMaxSize()){
                    Icon(
                        Icons.Default.Security, contentDescription = "Login",
                        modifier = Modifier.size(75.dp)
                            .align(alignment = Alignment.Center),
                        tint = Color(context.getColor(R.color.secondary))
                    )
                }
            }
        }
        Button(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 50.dp,
                    y = 400.dp
                )
                .requiredWidth(width = 263.dp)
                .requiredHeight(height = 47.dp),
            onClick = {
                keyboard?.hide()
                newPasswordViewModel.checkPassword()
            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(context.getColor(R.color.primaryDescendant)),
                contentColor = Color.White,
                disabledContainerColor = Color(context.getColor(R.color.primary)),
            ),
            enabled = enableCheck
        ) {
            /*Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(3.dp))
                    .background(color = Color(0xff1976d2)))*/
            Text(
                text = "CAMBIAR CONTRASEÑA",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 14.sp),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(align = Alignment.CenterVertically))
        }
        Input(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 48.dp,
                    y = 213.dp
                )
                .requiredWidth(width = 265.dp)
                .requiredHeight(height = 75.dp),
            label = "Nueva contraseña",
            placeholder = "●●●●●●●",
            value = password,
            type = KeyboardType.Password,
            onInputChanged = { newPasswordViewModel.checkPassword(it, password2) }
        )
        Input(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 48.dp,
                    y = 303.dp
                )
                .requiredWidth(width = 265.dp)
                .requiredHeight(height = 75.dp),
            label = "Confirmar contraseña",
            placeholder = "●●●●●●●",
            value = password2,
            type = KeyboardType.Password,
            onInputChanged = { newPasswordViewModel.checkPassword(password, it) }
        )
        //Texto de error
        Text(
            text = error,
            color = Color(0xFF21301),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(y = 55.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
        )

        Button(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .requiredWidth(width = 263.dp)
                .requiredHeight(height = 47.dp),
            onClick = {
                NavigationManager.instance!!.navigate("login")
            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(context.getColor(R.color.primaryDescendant)),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFA0CFFD),
            ),
        ) {
            /*Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(3.dp))
                    .background(color = Color(0xff1976d2)))*/
            Text(
                text = "CANCELAR",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 14.sp),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(align = Alignment.CenterVertically))
        }
    }
}

@Preview
@Composable
fun NewPasswordPreview() {
    NewPassword(newPasswordViewModel = NewPasswordViewModel())
}