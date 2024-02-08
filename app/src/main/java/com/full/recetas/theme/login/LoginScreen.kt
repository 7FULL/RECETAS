package com.full.recetas.theme.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.full.recetas.R
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.ui.theme.login.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun Login(loginViewModel: LoginViewModel) {
    val isLoading: Boolean by loginViewModel.isLoading.observeAsState(initial = true)

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
        Body(loginViewModel = loginViewModel)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Body(modifier: Modifier = Modifier, loginViewModel: LoginViewModel) {
    val enableLogin: Boolean by loginViewModel.enableLogin.observeAsState(initial = false)
    val password: String by loginViewModel.password.observeAsState(initial = "")
    val username: String by loginViewModel.username.observeAsState(initial = "")
    val error: String by loginViewModel.error.observeAsState(initial = "")

    val checked: Boolean by loginViewModel.checkRememberMe.observeAsState(initial = false)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            loginViewModel.signInWithGoogle(credential)
        } catch (e: ApiException) {
            //Aqui se pueden ver los codigos de error
            //https://developers.google.com/android/reference/com/google/android/gms/common/api/CommonStatusCodes

            //17 -> Error de red
            //16 -> Cliente desconectado
            //20 -> Servidor desconectado
            //10 -> Error de configuracion de proyecto
            //13 - 8 - 14 -> Error desconocido
            //5 -> Cuenta invalida
            //7 - 22 15 -> Error de red

            //Switch
            when(e.statusCode){
                17 -> loginViewModel.onErrorChanged("Parece que hay un error de red, comprueba tu conexion")
                16 -> loginViewModel.onErrorChanged("Parece que te has desconectado, vuelve a intentarlo")
                20 -> loginViewModel.onErrorChanged("Parece que el servidor no responde, intentelo mas tarde")
                10 -> loginViewModel.onErrorChanged("Parece que hay un error de configuracion, contacte con el administrador")
                13, 8, 14 -> loginViewModel.onErrorChanged("Parece que hay un error desconocido, contacte con el administrador")
                5 -> loginViewModel.onErrorChanged("Parece que tu cuenta no es valida, contacte con el administrador")
                7, 22, 15 -> loginViewModel.onErrorChanged("Parece que hay un error de red, comprueba tu conexion")
                else -> loginViewModel.onErrorChanged("Parece que hay un error iniciando sesion con google")
            }

            Log.i("CRM", "Inicio de sesion con google fallo", e)
        }
    }
    val tokenWeb = "52023902309-lg7u8pa3apvdhlgt4slhifm8vhrg6cc6.apps.googleusercontent.com"
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
                    y = (-44.5).dp
                )
                .requiredWidth(width = 329.dp)
                .requiredHeight(height = 413.dp),
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
        Button(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 50.dp,
                    y = 457.dp
                )
                .requiredWidth(width = 263.dp)
                .requiredHeight(height = 47.dp),
            onClick = {
                keyboard?.hide()
                loginViewModel.login()
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
                text = "INICIAR SESION",
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
                    y = 323.dp
                )
                .requiredWidth(width = 265.dp)
                .requiredHeight(height = 75.dp),
            label = "Contraseña",
            placeholder = "●●●●●●●",
            type = KeyboardType.Password,
            value = password,
            onInputChanged = { loginViewModel.onLoginChanged(username = username, password = it) }
        )

        Input(
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(y = 228.dp)
                .requiredWidth(width = 264.dp)
                .requiredHeight(height = 73.dp),
            label = "Usuario / Correo",
            placeholder = "Escribe tu usuario o correo",
            value = username,
            onInputChanged = { loginViewModel.onLoginChanged(username = it, password = password) }
        )


        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 50.dp,
                    y = 420.dp
                )
                .requiredWidth(width = 98.dp)
                .requiredHeight(height = 15.dp)
        ) {

            Box() {
                Checkbox(modifier = Modifier.offset(x = (-0).dp), checked = checked, onCheckedChange = { loginViewModel.onRememberMeChanged(it) })
                Text(
                    text = "Recuérdame",
                    color = Color(0xffbfc4cf),
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .offset(x = 20.dp, y= (-2).dp)
                        .padding(start = 19.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically))
            }
        }
        //TODO: Remember to remove it when the app is in production
        Button(
            onClick = {
                loginViewModel.onLoginChanged("admin", "678041577")
                loginViewModel.login()
            }
        ) {
            Text(text = "Empleado")
        }

        Button(
            modifier = Modifier.padding(start = 200.dp),
            onClick = {
                loginViewModel.onLoginChanged("admin2", "678041577")
                loginViewModel.login()
            }
        ) {
            Text(text = "Admin")
        }

        Text(
            text = "¿Has olvidado tu contraseña?",
            color = Color(context.getColor(R.color.secondary)),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 11.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(
                    x = 3.5.dp,
                    y = 525.dp
                )
                .wrapContentHeight(align = Alignment.CenterVertically)
                .clickable {
                    NavigationManager.instance!!.navigate("forgotPassword")
                }
        )
        //Texto de error
        Text(
            text = error,
            color = Color(0x99F21301),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(y = 575.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
        Box(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .requiredWidth(width = 263.dp)
                .requiredHeight(height = 47.dp)
                .clickable {
                    keyboard?.hide()
                    val opciones = GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(tokenWeb)
                        .requestEmail()
                        .build()
                    val cliente = GoogleSignIn.getClient(context, opciones)
                    launcher.launch(cliente.signInIntent)
                }
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 263.dp)
                    .requiredHeight(height = 47.dp)
                    .clip(shape = RoundedCornerShape(3.dp))
                    .background(color = Color(context.getColor(R.color.primaryDescendant))))
            Text(
                text = "INICIAR SESION CON GOOGLE",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 13.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 64.dp,
                        y = 15.dp
                    )
                    .wrapContentHeight(align = Alignment.CenterVertically))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 9.dp,
                        y = 5.dp
                    )
                    .requiredSize(size = 37.dp)
            ) {
                Box(
                    modifier = Modifier
                        .requiredSize(size = 37.dp)
                        .clip(shape = CircleShape)
                ){
                    Image(painter = painterResource(id = R.drawable.logogoogle_removebg), contentDescription = "Logo google")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input(modifier: Modifier = Modifier, label: String, placeholder: String,
          type: KeyboardType = KeyboardType.Text, onInputChanged: (String) -> Unit,
          value: String) {
    Box(
        modifier = modifier
    ) {
        Text(
            text = label,
            color = Color(0xffbfc4cf),
            style = TextStyle(
                fontSize = 14.sp),
            modifier = Modifier
                .requiredWidth(width = 245.dp))

        TextField(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 1.dp,
                    y = 27.dp
                )
                .requiredWidth(width = 263.dp)
                .requiredHeight(height = 46.dp)
                .clip(shape = RoundedCornerShape(3.dp))
                .background(color = Color(0xfff4f4f4)),
            colors = TextFieldDefaults.textFieldColors (
                containerColor = Color(0xfff4f4f4),
            ),
            keyboardOptions = KeyboardOptions(keyboardType = type),
            textStyle = TextStyle(fontSize = 14.sp),
            value = value,
            placeholder =
            {
                Text(
                    text = placeholder,
                    fontSize = 12.sp,
                )
            },
            onValueChange = { onInputChanged(it) },
            visualTransformation =
            if (type == KeyboardType.Password)
                PasswordVisualTransformation()
            else VisualTransformation.None,
        )
    }
}