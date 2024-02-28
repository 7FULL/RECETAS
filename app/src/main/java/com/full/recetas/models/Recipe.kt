package com.full.recetas.models

data class Recipe(
    var _id: String = "",
    val name: String = "",
    val publisher: User = User(),
    val image: String = "",
    val rating: Int = 0,
    val minutes: Int = 0,
    val description: String = "",
    val cookingInstructions: List<String> = listOf(),
    val ingredients: List<Ingredient> = listOf(),
    val likes: Int = 0,
    val tags: List<String> = listOf()
)
