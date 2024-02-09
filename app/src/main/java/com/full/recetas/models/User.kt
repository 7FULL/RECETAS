package com.full.recetas.models

data class User(
    var _id: String,
    var username: String,
    var password: String,
    var name: String,
    var surname: String,
    var email: String,
    var phone: String,
    var image: String,
    var recipes: List<Recipe>,
    var following: List<User>,
    var followers: List<User>,
    var token: String
)