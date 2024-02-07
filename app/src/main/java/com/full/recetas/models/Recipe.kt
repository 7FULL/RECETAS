package com.full.recetas.models

data class Recipe(
    val id: String,
    val name: String,
    val publisher: User,
    val image: String,
    val rating: Int,
    val minutes: Int,
    val description: String,
    val cookingInstructions: List<String>,
    val ingredients: List<String>,
    val likes: Int,
    val tags: List<String>
)
