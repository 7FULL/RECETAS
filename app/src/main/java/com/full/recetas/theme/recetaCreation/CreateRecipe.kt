package com.full.recetas.theme.recetaCreation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.input.KeyboardType
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
    val tags by vm.tags.observeAsState(listOf())
    val recipeName by vm.recipeName.observeAsState("")
    val recipeDescription by vm.recipeDescription.observeAsState("")
    val recipeMinutes by vm.recipeMinutes.observeAsState("")
    val selectedTags by vm.selectedTags.observeAsState(listOf())

    val instructions by vm.instructions.observeAsState(listOf())

    val ingredients by vm.ingredients.observeAsState(listOf())

    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val options = listOf("M", "H")
    val ingredientOptions = listOf("G", "KG", "ML", "L")

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0])}

    val selectedIndexes = remember { mutableStateListOf<Int>() }
    repeat(ingredientOptions.size) {
        selectedIndexes.add(0) // Inicialmente, todos los menús desplegables tienen el primer elemento seleccionado
    }

    // Estado para controlar si los menús desplegables están abiertos o cerrados
    val expandedStates = remember { mutableStateListOf<Boolean>() }
    repeat(ingredientOptions.size) {
        expandedStates.add(false) // Inicialmente, todos los menús desplegables están cerrados
    }

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

       if (active){
           SearchBar(
               modifier = Modifier
                   .semantics { traversalIndex = -1f }
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
               var list = tags!!.filter { it.contains(searchText) }

               if(list.isEmpty()){
                   // If no results we get the 5 first tags
                     list = tags!!.subList(0, 5)
               }

               repeat(list.size){
                   ListItem(
                       headlineContent = { Text(list[it]) },
                       leadingContent = { Icon(Icons.Filled.BakeryDining, contentDescription = null) },
                       modifier = Modifier
                           .clickable {
                               vm.selectTag(list[it])
                               active = false
                           }
                           .fillMaxWidth()
                           .padding(horizontal = 16.dp, vertical = 4.dp)
                   )
               }
           }
       }

        LazyColumn(modifier = Modifier.padding(innerPadding)){
            // Title
            item {
                Text(text = "CREAR RECETA",
                    style =
                    TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center),
                    modifier = Modifier
                        .padding(bottom = 25.dp)
                        .fillMaxWidth())
            }

            // Nombre de la receta
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

            // Descripción de la receta
            item {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)){
                    Input(
                        modifier = Modifier.align(alignment = Alignment.Center),
                        label = "Descripción de la receta",
                        placeholder = "Tu descripción (max 75 letras)",
                        onInputChanged = { vm.onChangeDescription(it) },
                        value = recipeDescription,
                        singleLine = false
                    )
                }
            }

            // Duración de la receta
            item{
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 75.dp, end = 75.dp, top = 50.dp)){
                    Row(modifier = Modifier.fillMaxWidth()){
                        Column{
                            Input(
                                label = "Duracion de la receta",
                                placeholder = if (selectedOptionText == "M") "Minutos" else "Horas",
                                onInputChanged = { vm.onChangeMinutes(it, selectedOptionText) },
                                value = recipeMinutes,
                                type = KeyboardType.Number,
                                modifier = Modifier
                                    .width(175.dp)
                                    .padding(end = 10.dp)
                            )
                        }
                        Column{
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                modifier = Modifier
                                    .padding(top = 27.dp)
                                    .height(56.dp),
                                onExpandedChange = { expanded = !expanded },
                            ){
                                TextField(
                                    modifier = Modifier.menuAnchor(),
                                    readOnly = true,
                                    value = selectedOptionText,
                                    textStyle = TextStyle(fontSize = 20.sp),
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

            // Tags
            item{
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 75.dp, end = 75.dp, top = 25.dp, bottom = 25.dp)){
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
                            if (!active){
                                SearchBar(
                                    modifier = Modifier
                                        .semantics { traversalIndex = -1f }
                                        .width(260.dp)
                                        .padding(top = 15.dp)
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
                                ){}
                            }
                        }
                        Row(modifier = Modifier.padding(top = 20.dp)){
                            LazyRow{
                                for (tag in selectedTags){
                                    item{
                                        InputChip(
                                            selected = true,
                                            onClick = {  },
                                            colors = InputChipDefaults.inputChipColors(selectedContainerColor = Color(context.getColor(R.color.primary))),
                                            label = { Text(tag) },
                                            trailingIcon = { Icon(Icons.Default.Cancel, contentDescription = null, modifier =Modifier.clickable { vm.selectTag(tag) }) },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Ingredientes
            item {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 75.dp, end = 75.dp)){
                    Column {
                        Row {
                            Text(
                                text = "Ingredientes",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                        Row {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)){
                                for (i in ingredients.indices){
                                    Column{
                                        Input(
                                            label = "Nombre del ingrediente",
                                            placeholder = "Nombre",
                                            onInputChanged = { vm.onChangeIngredient(name = it, index = i) },
                                            value = ingredients[i].name,
                                        )
                                    }
                                    Row(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 25.dp)){
                                        Column{
                                            Input(
                                                label = "Cantidad",
                                                placeholder = "200 G (Gramos)",
                                                onInputChanged = {
                                                    var quantityText = it

                                                    // We check for the ingredient option and add it to the quantity
                                                    if (selectedIndexes[i] != -1){
                                                        quantityText += " " + ingredientOptions[selectedIndexes[i]]
                                                    }

                                                    vm.onChangeIngredient(quantity = quantityText, index = i)
                                                                 },
                                                value = ingredients[i].name,
                                                type = KeyboardType.Number,
                                                modifier = Modifier
                                                    .width(175.dp)
                                                    .padding(end = 10.dp)
                                            )
                                        }
                                        Column{
                                            ExposedDropdownMenuBox(
                                                expanded = expandedStates[i],
                                                modifier = Modifier
                                                    .padding(top = 27.dp)
                                                    .height(56.dp),
                                                onExpandedChange = { expandedStates[i] = !expandedStates[i] },
                                            ){
                                                TextField(
                                                    modifier = Modifier.menuAnchor(),
                                                    readOnly = true,
                                                    value = ingredientOptions[selectedIndexes[i]],
                                                    textStyle = TextStyle(fontSize = 13.sp),
                                                    onValueChange = {},
                                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStates[i]) },
                                                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                                )
                                                ExposedDropdownMenu(
                                                    expanded = expandedStates[i],
                                                    onDismissRequest = { expandedStates[i] = false },
                                                ) {
                                                    for (option in ingredientOptions){
                                                        DropdownMenuItem(
                                                            text = { Text(option) },
                                                            onClick = {
                                                                selectedIndexes[i] = ingredientOptions.indexOf(option)
                                                                expandedStates[i] = false
                                                            },
                                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Button to remove ingredient
                                    Button(
                                        onClick = { vm.removeIngredient(i) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                    ) {
                                        Icon(Icons.Default.Cancel, contentDescription = null)
                                        Text("Eliminar ingrediente", modifier = Modifier.padding(start = 10.dp))
                                    }
                                }
                            }
                        }
                        Row {
                            // Button to add ingredient
                            Button(
                                onClick =
                                {
                                    vm.addIngredient()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = if (ingredients.isEmpty()) 0.dp else 35.dp)
                            ) {
                                Text("Agregar ingrediente")
                            }
                        }
                    }
                }
            }

            // Pasos
            item {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 75.dp, end = 75.dp)){
                    Column {
                        Row {
                            Text(
                                text = "Pasos",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                        Row {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)){
                                for (i in instructions.indices){
                                    Column{
                                        Input(
                                            label = "Paso a seguir",
                                            placeholder = "Paso",
                                            onInputChanged = { vm.onChangeInstruction(i, it) },
                                            value = instructions[i],
                                        )
                                    }

                                    // Button to remove ingredient
                                    Button(
                                        onClick = { vm.removeInstruction(i) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 25.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                    ) {
                                        Icon(Icons.Default.Cancel, contentDescription = null)
                                        Text("Eliminar paso", modifier = Modifier.padding(start = 10.dp))
                                    }
                                }
                            }
                        }
                        Row {
                            // Button to add ingredient
                            Button(
                                onClick =
                                {
                                    vm.addInstruction()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = if (ingredients.isEmpty()) 0.dp else 35.dp)
                            ) {
                                Text("Agregar paso")
                            }
                        }
                    }
                }
            }

            // Imagen
            item{
                Box(modifier = Modifier
                    .fillMaxWidth()){
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Row{
                            Text(
                                text = "Imagen",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),

                        ){
                            Column{
                                // Icon button of gallery
                                IconButton(
                                    onClick = {
                                        vm.requestImage()
                                    },
                                ) {
                                    Icon(Icons.Default.Image, contentDescription = null,
                                        modifier = Modifier.size(150.dp))
                                }
                            }

                            Column{
                                // Icon button of camera
                                IconButton(
                                    onClick = {
                                        vm.requestImage()
                                    },
                                ) {
                                    Icon(Icons.Default.CameraAlt, contentDescription = null,
                                        modifier = Modifier.size(150.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Button to create recipe
            item {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 75.dp, end = 75.dp, bottom = 25.dp)){
                    Button(
                        onClick = {
                            vm.createRecipe()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text("Crear receta")
                    }
                }
            }
        }
    }
}