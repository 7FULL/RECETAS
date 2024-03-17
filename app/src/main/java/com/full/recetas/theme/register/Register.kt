package com.full.recetas.theme.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.full.recetas.R
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.theme.login.Input

@Composable
fun Register(vm: RegisterViewModel) {
    val isLoading: Boolean by vm.isLoading.observeAsState(initial = true)

    if (isLoading){
        Box(
            Modifier
                .fillMaxSize()
        ) {
            CircularProgressIndicator(
                Modifier
                    .align(Alignment.Center)
                    .size(50.dp))
        }
    }else{
        Body(vm = vm)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Body(modifier: Modifier = Modifier, vm: RegisterViewModel) {
    val enableLogin: Boolean by vm.enableLogin.observeAsState(initial = false)
    val password: String by vm.password.observeAsState(initial = "")
    val username: String by vm.username.observeAsState(initial = "")
    val name: String by vm.name.observeAsState(initial = "")
    val surname: String by vm.surname.observeAsState(initial = "")
    val email: String by vm.email.observeAsState(initial = "")
    val error: String by vm.error.observeAsState(initial = "")
    val confirmPassword : String by vm.confirmPassword.observeAsState(initial = "")
    
    val context = LocalContext.current

    //Pillamos el teclado simplemente para poder ocultarlo
    val keyboard = LocalSoftwareKeyboardController.current

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
                .requiredWidth(width = 500.dp)
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
                    y = (100).dp
                )
                .requiredWidth(width = 329.dp)
                .requiredHeight(height = 800.dp),
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
                        Icons.Default.Login, contentDescription = "Login",
                        modifier = Modifier
                            .size(75.dp)
                            .align(alignment = Alignment.Center),
                        tint = Color(context.getColor(R.color.secondary))
                    )
                }
            }
        }


        LazyColumn(modifier = Modifier.padding(start = 50.dp, top = 225.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            item{
                Input(
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .requiredWidth(width = 264.dp)
                        .requiredHeight(height = 73.dp),
                    label = "Nomre de usuario",
                    placeholder = "Escribe tu usuario o correo",
                    value = username,
                    onInputChanged = { vm.onRegisterChanged(username = it, password = password, name = name, surname = surname, email = email, confirmPassword = confirmPassword) }
                )
            }

            item{
                Input(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .requiredWidth(width = 265.dp)
                        .requiredHeight(height = 75.dp),
                    label = "Contraseña",
                    placeholder = "●●●●●●●",
                    type = KeyboardType.Password,
                    value = password,
                    onInputChanged = { vm.onRegisterChanged(username = username, password = it, name = name, surname = surname, email = email, confirmPassword = confirmPassword) }
                )
            }

            item{
                Input(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .requiredWidth(width = 265.dp)
                        .requiredHeight(height = 75.dp),
                    label = "Confirmar contraseña",
                    placeholder = "●●●●●●●",
                    type = KeyboardType.Password,
                    value = confirmPassword,
                    onInputChanged = { vm.onRegisterChanged(username = username, password = password, name = name, surname = surname, email = email, confirmPassword = it) }
                )
            }

            item {
                Input(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .requiredWidth(width = 265.dp)
                        .requiredHeight(height = 75.dp),
                    label = "Nombre",
                    placeholder = "Escribe tu nombre",
                    value = name,
                    onInputChanged = { vm.onRegisterChanged(username = username, password = password, name = it, surname = surname, email = email, confirmPassword = confirmPassword) }
                )
            }

            item{
                Input(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .requiredWidth(width = 265.dp)
                        .requiredHeight(height = 75.dp),
                    label = "Apellido",
                    placeholder = "Escribe tu apellido",
                    value = surname,
                    onInputChanged = { vm.onRegisterChanged(username = username, password = password, name = name, surname = it, email = email, confirmPassword = confirmPassword) }
                )
            }

            item{
                Input(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .requiredWidth(width = 265.dp)
                        .requiredHeight(height = 75.dp),
                    label = "Correo",
                    placeholder = "Escribe tu correo",
                    value = email,
                    onInputChanged = { vm.onRegisterChanged(username = username, password = password, name = name, surname = surname, email = it, confirmPassword = confirmPassword) }
                )
            }

            item{
                Button(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .requiredWidth(width = 263.dp)
                        .requiredHeight(height = 47.dp),
                    onClick = {
                        keyboard?.hide()
                        vm.register()
                    },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(context.getColor(R.color.primaryDescendant)),
                        contentColor = Color.White,
                        disabledContainerColor = Color(context.getColor(R.color.primary)),
                    ),
                    enabled = enableLogin
                ) {
                    /*Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(shape = RoundedCornerShape(3.dp))
                            .background(color = Color(0xff1976d2)))*/
                    Text(
                        text = "Registrarse",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 14.sp),
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentHeight(align = Alignment.CenterVertically))
                }
            }

            item{
                //Texto de error
                Text(
                    text = error,
                    color = Color(0x99F21301),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
            }
        }
    }
}

