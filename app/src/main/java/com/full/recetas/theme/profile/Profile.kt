package com.full.recetas.theme.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.full.recetas.models.User
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.network.API

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Profile(vm: ProfileViewModel) {
    val user by vm.user.observeAsState(User())
    val recipes by vm.recipes.observeAsState(listOf())
    val context = LocalContext.current

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
            }
        },
        bottomBar = {
            BottomBar(selectedIcon = 0)
        }
    ) {
            innerPadding ->

        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ){
                // Profile Image
                Row{
                    Box(modifier = Modifier.fillMaxWidth()){
                        GlideImage(
                            model = user.image,
                            contentDescription = user.name,
                            contentScale = ContentScale.Crop,
                            loading = placeholder(R.drawable.loading),
                            modifier = Modifier
                                .height(125.dp)
                                .width(125.dp)
                                .align(alignment = Alignment.Center)
                                .clip(shape = RoundedCornerShape(125.dp))
                        )
                    }
                }

                // Name
                Row {
                    Box(modifier = Modifier.fillMaxWidth()){
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 30.sp,
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .align(alignment = Alignment.Center)
                        )
                    }
                }

                // Username
                Row {
                    Box(modifier = Modifier.fillMaxWidth()){
                        Text(
                            text = "@" + user.username,
                            style = MaterialTheme.typography.displayMedium,
                            fontSize = 20.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .align(alignment = Alignment.Center)
                        )
                    }
                }

                // The edit profile and logout buttons
                Row {
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                        Column {
                            Box(){
                                Button(
                                    onClick = { NavigationManager.instance!!.navigate("viewProfile") },
                                    modifier = Modifier
                                        .padding(top = 10.dp, end = 10.dp)
                                        .align(alignment = Alignment.Center)
                                ) {
                                    Text(
                                        text = "Editar perfil",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontSize = 20.sp
                                    )
                                }
                            }
                        }

                        Column {
                            Box(){
                                Button(
                                    onClick = { API.logout() },
                                    modifier = Modifier
                                        .padding(top = 10.dp, start = 10.dp)
                                        .align(alignment = Alignment.Center)
                                ) {
                                    Text(
                                        text = "Cerrar sesión",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontSize = 20.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Followers and following
                Row{
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Box(){
                                Text(
                                    text = "Seguidores: " + user.followers.size,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .padding(top = 10.dp, end = 10.dp)
                                        .align(alignment = Alignment.Center)
                                )
                            }
                        }

                        Column {
                            Box(){
                                Text(
                                    text = "Siguiendo: " + user.following.size,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .padding(top = 10.dp, start = 10.dp)
                                        .align(alignment = Alignment.Center)
                                )
                            }
                        }
                    }
                }

                // Recipes
                Row{
                    if (recipes.isNotEmpty()){
                        Column {
                            Row {
                                Box(modifier = Modifier.fillMaxWidth()){
                                    Text(
                                        text = "Recetas",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontSize = 20.sp,
                                        modifier = Modifier
                                            .padding(top = 10.dp)
                                            .align(alignment = Alignment.Center)
                                    )
                                }
                            }
                            Row {
                                LazyColumn(modifier = Modifier
                                    .padding(
                                        start = 11.dp, end = 11.dp
                                    )
                                    .height(375.dp)
                                ){
                                    for (receta in recipes){
                                        item{
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(120.dp)
                                                    .padding(bottom = 11.dp)
                                                    .clickable {
                                                        NavigationManager.instance?.navigate(
                                                            "recipe?id=${receta._id}"
                                                        )
                                                    }
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
                                                                .background(
                                                                    color = Color(
                                                                        context.getColor(
                                                                            R.color.primary
                                                                        )
                                                                    )
                                                                )){
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
                                                                    .background(
                                                                        color = Color(
                                                                            context.getColor(
                                                                                R.color.primary
                                                                            )
                                                                        )
                                                                    )
                                                            )

                                                            Icon(
                                                                Icons.Default.DeleteForever, contentDescription = "Borrar",
                                                                modifier = Modifier
                                                                    .align(alignment = Alignment.TopStart)
                                                                    .requiredSize(size = 35.dp)
                                                                    .padding(5.dp)
                                                                    .clickable { vm.deleteRecipe(receta) }
                                                                ,
                                                            )

                                                            Icon(
                                                                Icons.Default.Edit, contentDescription = "Borrar",
                                                                modifier = Modifier
                                                                    .align(alignment = Alignment.BottomStart)
                                                                    .requiredSize(size = 35.dp)
                                                                    .padding(5.dp)
                                                                    .clickable { vm.editRecipe(receta) }
                                                                ,
                                                                tint = Color.White
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
                                                                .background(
                                                                    color = Color(
                                                                        context.getColor(
                                                                            R.color.primary
                                                                        )
                                                                    )
                                                                )
                                                        ){
                                                            Icon(
                                                                Icons.Default.Favorite, contentDescription = "Likes",
                                                                modifier = Modifier
                                                                    .align(alignment = Alignment.TopEnd)
                                                                    .requiredSize(size = 35.dp)
                                                                    .padding(5.dp),
                                                                tint = Color.Red,
                                                            )

                                                            Text(text = receta.likes.toString(),
                                                                color = Color.White,
                                                                modifier = Modifier
                                                                    .align(alignment = Alignment.TopEnd)
                                                                    .padding(5.dp)
                                                                    .padding(end = 25.dp)
                                                                ,
                                                                style = TextStyle(
                                                                    fontSize = 16.sp,
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                            )
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
                                                                            .padding(
                                                                                top = 5.dp,
                                                                                bottom = 5.dp
                                                                            )
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
                                                                                            context.getColor(
                                                                                                R.color.primaryDescendant
                                                                                            )
                                                                                        )
                                                                                    )
                                                                                ){
                                                                                    Text(text = tag,
                                                                                        color = Color.White,
                                                                                        modifier = Modifier
                                                                                            .fillMaxWidth()
                                                                                            .align(
                                                                                                Alignment.Center
                                                                                            )
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
                    }else{
                        Box(modifier = Modifier.fillMaxWidth()){
                            Text(
                                text = "No hay recetas",
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .align(alignment = Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}