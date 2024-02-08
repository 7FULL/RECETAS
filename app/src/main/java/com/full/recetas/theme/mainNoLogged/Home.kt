package com.full.recetas.theme.mainNoLogged

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.full.recetas.R
import com.full.recetas.BottomBar
import com.full.recetas.models.Recipe

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Home(modifier: Modifier = Modifier, vm: HomeViewModel, loggedIn: Boolean = false) {
    val searchBarText: String by vm.searchBarText.observeAsState("")
    var searchBarActive by rememberSaveable { mutableStateOf(false) }

    val searchRecipes: MutableList<Recipe> by vm.auxRecipes.observeAsState(mutableListOf())

    val context = LocalContext.current

    Scaffold (
        topBar = {
            Box(
                modifier = Modifier
                    .requiredHeight(height = 70.dp)
                    .fillMaxWidth()
                    .background(color = Color(context.getColor(R.color.primary)))
            ){
                Column{
                    Box(modifier = Modifier.fillMaxSize()){
                        Image(painter = painterResource(
                            id = R.drawable.restaurant),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .align(alignment = Alignment.CenterStart)
                                .padding(start = 20.dp)
                                .requiredSize(size = 50.dp)
                        )
                    }
                }

                Column{
                    Box(modifier = Modifier.fillMaxSize()
                    ){
                        SearchBar(
                            modifier = Modifier
                                .padding(bottom = 5.dp, end = 20.dp)
                                .width(300.dp)
                                .align(alignment = Alignment.CenterEnd)
                                .background(color = Color(context.getColor(R.color.primary))),
                            query = searchBarText,
                            onQueryChange = {
                                vm.onSearchBarTextChanged(it)
                            },
                            onSearch = {
                                searchBarActive = false

                                vm.filterRecipes()
                            },
                            active = searchBarActive,
                            onActiveChange = {
                                searchBarActive = it
                            },
                            placeholder = { Text("Encontrar recetas...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Mic,
                                    contentDescription = "Microphone",
                                    modifier = Modifier
                                        .clickable { /* TODO: Maybe implement the function of the microphone */ }
                                )
                            },
                        ) {
                            if (searchRecipes != null){
                                repeat(searchRecipes.size) { idx ->
                                    val resultText = searchRecipes[idx].name    ?: "Esta factura no tiene nombre"
                                    ListItem(
                                        headlineContent = { Text(resultText) },
                                        supportingContent = { Text(searchRecipes[idx].description) },
                                        leadingContent = { Icon(Icons.Filled.AttachMoney, contentDescription = null) },
                                        modifier = Modifier
                                            .clickable {
                                                searchBarActive = false

                                                vm.onSearchBarTextChanged(resultText)
                                            }
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomBar(selectedIcon = -1)
        }
    ) {
        innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)){

            //Titulo recetas de la semamna
            Row {
                Text(text = "Recetas de la semana",
                    modifier = Modifier.padding(start = 15.dp, top = 5.dp),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            //Recetas de la semana
            Row {
                LazyRow(modifier = Modifier.padding(
                    start= 11.dp, top= 20.dp, bottom = 20.dp)){
                    for (i in 0..5){
                        item{
                            Box(modifier = Modifier.padding(end = 20.dp)){
                                Column(
                                    modifier = Modifier
                                        .requiredSize(size = 120.dp)
                                        .width(138.dp)
                                        .clip(shape = RoundedCornerShape(10.dp))
                                        .background(color = Color(context.getColor(R.color.unselected)))
                                ) {
                                    Row{
                                        Image(
                                            painter = painterResource(id = R.drawable.receta_default),
                                            contentDescription = "Recipe",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .height(74.dp)
                                        )
                                    }
                                    Row(modifier = Modifier.fillMaxSize()){
                                        Text(
                                            text = "Receta $i",
                                            modifier = Modifier
                                                .align(Alignment.CenterVertically)
                                                .fillMaxWidth(),
                                            style = TextStyle(
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            )
                                        )
                                    }
                                }
                            }
                            }
                    }
                }
            }

            //Categorias
            Row {
                LazyRow(modifier = Modifier.padding(11.dp, top = 0.dp, bottom = 20.dp)){
                    for (i in 0..5){
                        item{
                            Box(modifier = Modifier
                                .padding(end = 11.dp)
                                .align(Alignment.CenterVertically)
                                .clip(shape = RoundedCornerShape(15.dp))
                                .requiredWidth(width = 104.dp)
                                .requiredHeight(height = 36.dp)
                                .background(color = Color(context.getColor(R.color.primary)))
                            ){
                                Text(text = "Categoria $i",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Center)
                                    ,
                                    style = TextStyle(
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }
                }
            }

            //Recetas
            Row{
                LazyColumn(modifier = Modifier
                    .padding(
                        start = 11.dp, end = 11.dp
                    )
                    .height(if (!loggedIn) 402.dp else 450.dp)
                ){
                    for (i in 0..5){
                        item{
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .padding(bottom = 11.dp)
                            ) {
                                Row{
                                    Column{
                                        Box(
                                            modifier = Modifier
                                                .width(120.dp)
                                                .fillMaxHeight()
                                                .clip(
                                                    shape = RoundedCornerShape(
                                                        topStart = 10.dp,
                                                        bottomStart = 10.dp
                                                    )
                                                )
                                                .background(color = Color(context.getColor(R.color.primary)))){
                                            Image(
                                                painter = painterResource(id = R.drawable.receta_default),
                                                contentDescription = "Recipe",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(
                                                        shape = RoundedCornerShape(
                                                            topStart = 10.dp,
                                                            bottomStart = 10.dp
                                                        )
                                                    )
                                                    .background(color = Color(context.getColor(R.color.primary)))
                                            )
                                            Image(
                                                Icons.Default.FavoriteBorder,
                                                contentDescription = "favorite",
                                                colorFilter = ColorFilter.tint(Color.White),
                                                modifier = Modifier
                                                    .align(alignment = Alignment.TopStart)
                                                    .requiredSize(size = 35.dp)
                                                    .padding(5.dp)
                                            )
                                        }
                                    }

                                    Column{
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(
                                                    shape = RoundedCornerShape(
                                                        topEnd = 10.dp,
                                                        bottomEnd = 10.dp
                                                    )
                                                )
                                                .background(color = Color(context.getColor(R.color.primary)))
                                        ){
                                            Column(modifier = Modifier.padding(start= 11.dp, end = 11.dp, top = 5.dp)){
                                                Row{
                                                    Text(
                                                        text = "Lorem ipsum",
                                                        color = Color.Black,
                                                        textAlign = TextAlign.Center,
                                                        style = TextStyle(
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight.Bold,
                                                        ),
                                                        modifier = Modifier
                                                            .wrapContentHeight(align = Alignment.CenterVertically)
                                                    )
                                                }
                                                Row{
                                                    Text(
                                                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc dapibus mauris ut sagittis lobortis. Proin iaculis sollicitudin mauris eu imperdiet.",
                                                        color = Color.Black,
                                                        style = TextStyle(
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Light),
                                                        modifier = Modifier
                                                            .width(231.dp)
                                                            .height(50.dp)
                                                            .padding(top = 5.dp, bottom = 5.dp)
                                                    )
                                                }

                                                Row {
                                                    LazyRow(modifier = Modifier.padding()){
                                                        for (i in 0..5){
                                                            item{
                                                                Box(modifier = Modifier
                                                                    .padding(end = 11.dp)
                                                                    .align(Alignment.CenterVertically)
                                                                    .clip(
                                                                        shape = RoundedCornerShape(
                                                                            20.dp
                                                                        )
                                                                    )
                                                                    .width(89.dp)
                                                                    .height(23.dp)
                                                                    .background(color = Color(context.getColor(R.color.primaryDescendant)))
                                                                ){
                                                                    Text(text = "Categoria $i",
                                                                        color = Color.White,
                                                                        modifier = Modifier
                                                                            .fillMaxWidth()
                                                                            .align(Alignment.Center)
                                                                        ,
                                                                        style = TextStyle(
                                                                            fontSize = 12.sp,
                                                                            textAlign = TextAlign.Center
                                                                        )
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }


                            }
                        }
                    }
                }
            }

            //Mensaje de login
            if(!loggedIn){
                Row{
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .background(color = Color(context.getColor(R.color.secondary)))
                    ){
                        Row(modifier = Modifier.align(Alignment.Center)){
                            Column{
                                Box(modifier = Modifier.fillMaxHeight()){
                                    Text(
                                        text = "¿Todavia no tienes cuenta? Unete!",
                                        style = TextStyle(
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            textAlign = TextAlign.Center
                                        ),
                                        color = Color.White,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                            Column{
                                Box(modifier = Modifier.padding(start = 15.dp, top=5.dp, bottom = 5.dp)){
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = Color.White,
                                                shape = RoundedCornerShape(5.dp)
                                            )
                                            .height(22.dp)
                                    )
                                    {
                                        Text(
                                            text = "Iniciar sesion",
                                            color = Color(context.getColor(R.color.secondary)),
                                            style = TextStyle(
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                textAlign = TextAlign.Center
                                            ),
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .padding(start = 5.dp, end = 5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MainNoLoggedPreview() {
    Home(vm = HomeViewModel())
}


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
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xfff4f4f4),
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