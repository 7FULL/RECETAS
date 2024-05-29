package com.full.recetas.models

data class User(
    var _id: String = "",
    var username: String = "",
    var password: String = "",
    var name: String = "",
    var surname: String = "",
    var email: String = "",
    var image: String = "",
    var likes: ArrayList<String> = arrayListOf(),
    var recipes: List<String> = listOf(),
    var following: ArrayList<String> = arrayListOf(),
    var followers: ArrayList<String> = arrayListOf(),
    var token: String = ""
)