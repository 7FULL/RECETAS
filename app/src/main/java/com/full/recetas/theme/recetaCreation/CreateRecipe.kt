package com.full.recetas.theme.recetaCreation

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
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.full.recetas.BottomBar
import com.full.recetas.R
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.network.API
import com.full.recetas.theme.home.Input

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRecipe(vm: CreateRecipeViewModel) {
    val tags = vm.tags.value
    val recipeName by vm.recipeName.observeAsState("")
    val recipeMinutes by vm.recipeMinutes.observeAsState("")
    val selectedTags by vm.selectedTags.observeAsState(listOf())

    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val options = listOf("M", "H")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0])}

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
            BottomBar(selectedIcon = 2)
        }
    ) {
        innerPadding ->

        //TODO: Tener 2 searchbars una sin scrolls dentro y en su posicion y otra arriba e ir activandolas y desactivandolas
        SearchBar(
            modifier = Modifier
                .semantics { traversalIndex = -1f }
                .width(250.dp)
                .offset(x = 85.dp, y = 420.dp)
            ,
            query = searchText,
            onQueryChange = { searchText = it },
            onSearch = { active = false },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = { Text("Nombre de la tag") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = { Icon(Icons.Default.Mic, contentDescription = null) },
        ) {
            repeat(4) { idx ->
                val resultText = "Suggestion $idx"
                ListItem(
                    headlineContent = { Text(resultText) },
                    supportingContent = { Text("Additional info") },
                    leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                    modifier = Modifier
                        .clickable {
                            searchText = resultText
                            active = false
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

        LazyColumn(modifier = Modifier.padding(innerPadding)){
            item {
                Text(text = "CREAR RECETA",
                    style =
                    TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center),
                    modifier = Modifier
                        .padding(25.dp)
                        .fillMaxWidth())
            }

            item {
                Box(modifier = Modifier.fillMaxWidth()){
                    Input(
                        modifier = Modifier.align(alignment = Alignment.Center),
                        label = "¿ Como se llama tu receta ?",
                        placeholder = "Nombre de la receta",
                        onInputChanged = { vm.onChangeName(it) },
                        value = recipeName
                    )
                }
            }

            item{
                Box(modifier = Modifier.fillMaxWidth().padding(start=75.dp, end=75.dp, top = 50.dp)){
                    Row(modifier = Modifier.fillMaxWidth()){
                        Column{
                            Input(
                                label = "Duracion de la receta",
                                placeholder = if (selectedOptionText == "M") "Minutos" else "Horas",
                                onInputChanged = { vm.onChangeMinutes(it, selectedOptionText) },
                                value = recipeMinutes,
                                modifier = Modifier.width(175.dp).padding(end = 10.dp)
                            )
                        }
                        Column{
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                modifier = Modifier.padding(top=27.dp).height(46.dp),
                                onExpandedChange = { expanded = !expanded },
                            ){
                                TextField(
                                    // The `menuAnchor` modifier must be passed to the text field for correctness.
                                    modifier = Modifier.menuAnchor(),
                                    readOnly = true,
                                    value = selectedOptionText,
                                    textStyle = TextStyle(fontSize = 14.sp),
                                    onValueChange = {},
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                )
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                ) {
                                    options.forEach { selectionOption ->
                                        DropdownMenuItem(
                                            text = { Text(selectionOption) },
                                            onClick = {
                                                selectedOptionText = selectionOption
                                                expanded = false
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item{
                Box(modifier = Modifier.fillMaxWidth().padding(start=75.dp, end=75.dp, top = 25.dp, bottom = 50.dp)){
                    Column(modifier = Modifier.fillMaxWidth()){
                        Row{
                            Text(
                                text = "Selecciona los tags que mejor describan tu receta",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                        Row{
                            LazyRow{
                                for (tag in selectedTags){
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
        }
    }
}