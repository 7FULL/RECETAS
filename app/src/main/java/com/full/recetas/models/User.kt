package com.full.recetas.models

data class User(
    var _id: String = "",
    var username: String = "",
    var password: String = "",
    var name: String = "",
    var surname: String = "",
    var email: String = "",
    var phone: String = "",
    var image: String = "",
    var likes: ArrayList<String> = arrayListOf(),
    var recipes: List<Recipe> = listOf(),
    var following: List<User> = listOf(),
    var followers: List<User> = listOf(),
    var token: String = ""
){
}