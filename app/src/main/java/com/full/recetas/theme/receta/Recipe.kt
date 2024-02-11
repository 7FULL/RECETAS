package com.full.recetas.theme.receta

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import com.full.recetas.models.Recipe
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.network.API

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Recipe(vm: RecipeViewModel) {
    val context = LocalContext.current
    val recipe: Recipe by vm.recipeData.observeAsState(Recipe())
    val likes = recipe.likes

    val isLikedLD: Boolean by vm.isLiked.observeAsState(false)

    Log.i("Recipe", "isLiked: $isLikedLD")

    //If likes is greater than 1000, we divide it by 1000 and add a "K" at the end
    val likesText = if (likes > 1000) "${likes / 1000}K" else likes.toString()

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

                        Text(
                            text = recipe.name,
                            modifier = Modifier.align(alignment = Alignment.Center),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 24.sp
                            )
                        )
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
            Box{
                GlideImage(model = recipe.image,
                    contentDescription = recipe.name,
                    contentScale = ContentScale.Crop,
                    loading = placeholder(R.drawable.loading),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )

                if (API.isLogged){
                    Box(
                        modifier = Modifier
                            .offset(y = (250).dp)
                    ) {
                        Row(modifier = Modifier
                            .background(color = Color(0, 0, 0, 100))
                            .width(100.dp)
                            .height(50.dp)){
                            Column {
                                //If we have it in our likes, we show the filled heart
                                if (isLikedLD) {
                                    Icon(
                                        Icons.Default.Favorite, contentDescription = "Likes",
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .size(40.dp)
                                            .clickable {
                                                vm.unlikeRecipe(recipe._id)
                                            },
                                        tint = Color.Red,
                                    )
                                }else{
                                    Icon(
                                        Icons.Default.FavoriteBorder, contentDescription = "Likes",
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .size(40.dp)
                                            .clickable {
                                                vm.likeRecipe(recipe._id)
                                            },
                                        tint = Color.White,
                                    )
                                }
                            }
                            Column{
                                Box(modifier = Modifier.fillMaxSize()){
                                    Text(
                                        text = likesText,
                                        modifier = Modifier.align(alignment = Alignment.Center),
                                        style = TextStyle(
                                            color = Color.White,
                                            fontSize = 24.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row{
                Column(modifier = Modifier.padding(10.dp)){
                    ElevatedCard {
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth()
                        ) {
                            //Publisher
                            Row{
                                GlideImage(model = recipe.publisher.image,
                                    contentDescription = "Publisher",
                                    contentScale = ContentScale.Crop,
                                    loading = placeholder(R.drawable.loading),
                                    modifier = Modifier
                                        .size(45.dp)
                                        .clip(shape = RoundedCornerShape(25.dp))
                                        .align(alignment = Alignment.CenterVertically)
                                        .padding(start = 10.dp)
                                )

                                Text(
                                    text = recipe.publisher.username,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        fontSize = 24.sp,
                                        textAlign = TextAlign.Center
                                    ),
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .align(alignment = Alignment.CenterVertically)
                                        .width(175.dp)
                                )
                                if(API.isLogged){
                                    //Suscribe button if the publisher followers list doesn't contain the current user
                                    if (!recipe.publisher.followers.contains(API.User.value)){
                                        Button(onClick = { /*TODO*/ },
                                            modifier = Modifier
                                                .padding(start = 10.dp)
                                                .align(alignment = Alignment.CenterVertically),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(context.getColor(R.color.secondary))
                                            )
                                        ){
                                            Text(text = "Suscribirse",
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
                                                    textAlign = TextAlign.Center
                                                )
                                            )
                                        }
                                    }else{
                                        Button(onClick = { /*TODO*/ },
                                            modifier = Modifier
                                                .padding(start = 10.dp)
                                                .align(alignment = Alignment.CenterVertically),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(context.getColor(R.color.secondary))
                                            )
                                        ){
                                            Text(text = "Desuscribirse",
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 16.sp,
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

            Row{
                Column(modifier = Modifier.padding(5.dp)){
                    ElevatedCard {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .width(150.dp)
                        ) {
                            Text(
                                text = "Ingredientes",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    textAlign = TextAlign.Center
                                )
                            )

                            LazyColumn(modifier = Modifier.height(93.dp)){
                                items(recipe.ingredients.size) { index ->
                                    Text(
                                        text = recipe.ingredients[index],
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontSize = 18.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                Column(modifier = Modifier.padding(5.dp)){
                    ElevatedCard {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .width(150.dp)
                        ){
                            Text(text = "INFO",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp),
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    textAlign = TextAlign.Center
                                )
                            )

                            Row(modifier = Modifier
                                .align(alignment = Alignment.CenterHorizontally)
                                .padding(top = 5.dp)){
                                Icon(Icons.Default.Timer, contentDescription = "Time")
                                Text(
                                    text = recipe.minutes.toString() + " M",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 18.sp
                                    ),
                                    modifier = Modifier.padding(start = 10.dp)
                                )
                            }

                            Row(modifier = Modifier
                                .align(alignment = Alignment.CenterHorizontally)
                                .padding(top = 5.dp)){
                                val rating = "⭐".repeat(recipe.rating)

                                Text(
                                    text = rating,
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 18.sp
                                    ),
                                    modifier = Modifier.padding(start = 10.dp)
                                )
                            }

                            Row(modifier = Modifier
                                .align(alignment = Alignment.CenterHorizontally)
                                .padding(top = 5.dp)){
                                Text(
                                    text = recipe.cookingInstructions.size.toString() + " Pasos",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 18.sp
                                    ),
                                    modifier = Modifier.padding(start = 10.dp)
                                )
                            }
                        }
                    }
                }
            }

            Row{
                Column(modifier = Modifier.padding(5.dp)){
                    ElevatedCard {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                        ) {
                            LazyRow{
                                for (tag in recipe.tags){
                                    item{
                                        Box(modifier = Modifier
                                            .padding(end = 10.dp)
                                            .clip(shape = RoundedCornerShape(15.dp))
                                            .requiredWidth(width = 104.dp)
                                            .requiredHeight(height = 36.dp)
                                            .background(color = Color(context.getColor(R.color.primary)))
                                        ){
                                            Text(text = tag,
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
                    }
                }
            }

            Row{
                Button(onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(context.getColor(R.color.secondary))
                    )
                ){
                    Icon(Icons.Default.Start,
                        contentDescription = "Start",
                        tint = Color.Black,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Text(text = "Empezar a cocinar",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}