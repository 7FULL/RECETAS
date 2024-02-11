package com.full.recetas.theme.likes

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.full.recetas.BottomBar
import com.full.recetas.R
import com.full.recetas.models.Recipe
import com.full.recetas.navigation.AppScreens
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.network.API
import com.full.recetas.network.API.trendingRecipes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun Likes(vm: LikesViewModel) {
    val likes: MutableList<Recipe> by vm.auxLikes.observeAsState(mutableListOf())
    val context = LocalContext.current

    val searchBarText: String by vm.searchBarText.observeAsState("")
    var searchBarActive by rememberSaveable { mutableStateOf(false) }

    val isLoading: Boolean? by vm.isLoading.observeAsState(true)

    Scaffold (
        topBar = {
            Box(
                modifier = Modifier
                    .requiredHeight(height = 70.dp)
                    .fillMaxWidth()
            ){
                Column{
                    Box(modifier = Modifier.fillMaxSize()){
                        Image(
                            Icons.Default.ArrowBackIos,
                            contentDescription = "Logo",
                            modifier = Modifier
                                .align(alignment = Alignment.CenterStart)
                                .padding(start = 20.dp)
                                .requiredSize(size = 50.dp)
                                .clickable { NavigationManager.instance!!.popBackStack() }
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
                        ,
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
                            if (likes != null){
                                repeat(likes.size) { idx ->
                                    val resultText = likes[idx].name    ?: "Esta factura no tiene nombre"
                                    ListItem(
                                        headlineContent = { Text(resultText) },
                                        supportingContent = { Text(likes[idx].description) },
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
            BottomBar(selectedIcon = 1)
        }
    ) {
        innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)){
            if(isLoading!!){
                GlideImage(model = R.drawable.loading,
                    contentDescription = "Loading",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }else{
                //Recetas
                Row{
                    LazyColumn(modifier = Modifier
                        .padding(
                            start = 11.dp, end = 11.dp
                        )
                    ){
                        for (receta in likes){
                            item{
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .padding(bottom = 11.dp)
                                        .clickable { NavigationManager.instance?.navigate("recipe?id=${receta._id}") }
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
                                                GlideImage(model = receta.image,
                                                    contentDescription = "Recipe",
                                                    contentScale = ContentScale.Crop,
                                                    loading = placeholder(R.drawable.loading),
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
                                                            text = receta.name,
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
                                                            text = receta.description,
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
                                                            for (tag in receta.tags){
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
                                                                        .background(
                                                                            color = Color(
                                                                                context.getColor(R.color.primaryDescendant)
                                                                            )
                                                                        )
                                                                    ){
                                                                        Text(text = tag,
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
            }
        }
    }
}