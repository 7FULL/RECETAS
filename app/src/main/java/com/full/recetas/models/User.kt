package com.full.recetas.models

data class User(
    var id:String,
    var username: String,
    var password: String,
    var name: String,
    var surname: String,
    var email: String,
    var phone: String,
    var image: String,
    var recipes: List<Recipe>,
    var followers: List<User>,
)