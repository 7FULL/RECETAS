package com.full.recetas.theme.receta

import androidx.compose.runtime.Composable
import com.full.recetas.R
import com.full.recetas.models.Recipe
import com.full.recetas.models.User

class RecipeViewModel {

    @Composable
    fun getrecipe(id: String): Recipe {
        return Recipe("1", "Macarrones con chorizo",
            User("1", "JuanManolo24", "", "Juan", "Manolo", "email@gmail.com", "678 04 15 77", R.drawable.logogoogle_removebg.toString(), listOf(), listOf(), listOf(), ""),
            R.drawable.receta_default.toString(),
            4,
            30,
            "Esto es una descirpcion",
            listOf("Paso 1", "Paso 2", "Paso 3"),
            listOf("Macarrones", "Chorizo", "Tomate", "Cebolla", "Pimiento", "Aceite", "Sal", "Pimienta"),
            5324,
            listOf("Pasta", "Cena", "Horno"),
        )
    }
}