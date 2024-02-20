package com.full.recetas.theme.recetaCreation

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.recetas.models.Ingredient
import com.full.recetas.navigation.AppScreens
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.network.API
import kotlinx.coroutines.launch

class CreateRecipeViewModel: ViewModel() {

    // Recipe data
    val _recipeName = MutableLiveData<String>()
    val recipeName: LiveData<String> = _recipeName

    val _recipeDescription = MutableLiveData<String>()
    val recipeDescription: LiveData<String> = _recipeDescription

    val _recipeMinutes = MutableLiveData<String>()
    val recipeMinutes: LiveData<String> = _recipeMinutes

    // All tags
    private val _tags = MutableLiveData<List<String>>()
    val tags: LiveData<List<String>> = _tags

    // Tags selected
    private val _selectedTags = MutableLiveData<List<String>>()
    val selectedTags: LiveData<List<String>> = _selectedTags

    private val _ingredients = MutableLiveData<List<Ingredient>>()
    val ingredients: LiveData<List<Ingredient>> = _ingredients

    private val _instructions = MutableLiveData<List<String>>()
    val instructions: LiveData<List<String>> = _instructions
    init {
        if (!API.isLogged){
            NavigationManager.instance!!.navigate(AppScreens.Login.route)
        }else{
            // Get tags
            getTags()
        }
    }

    // Select tag
    fun selectTag(tag: String){
        val list = _selectedTags.value?.toMutableList() ?: mutableListOf()

        if(list.contains(tag)){
            list.remove(tag)
        }else{
            list.add(tag)
        }

        _selectedTags.value = list
    }

    // Get tags
    private fun getTags(){
        viewModelScope.launch{
            val response = API.service.getTags()

            if(response.isSuccessful){
                val data = response.body()!!

                if(data.code == 200){
                    _tags.value = data.data!!.map { it.name }
                }
            }
        }
    }

    // Create recipe
    fun createRecipe(){
        if(isValid()){
            val recipe = com.full.recetas.models.Recipe(
                name = recipeName.value!!,
                description = recipeDescription.value!!,
                minutes = recipeMinutes.value!!.toInt(),
                tags = selectedTags.value!!,
                ingredients = ingredients.value!!,
                cookingInstructions = instructions.value!!
            )

            viewModelScope.launch {
                val response = API.service.createRecipe(recipe)

                if(response.isSuccessful){
                    val data = response.body()!!

                    if(data.code == 200){
                        Toast.makeText(API.mainActivity, "Receta creada", Toast.LENGTH_SHORT).show()
                        NavigationManager.instance!!.navigate(AppScreens.Recipe.route + "/${data.data!!._id}")
                    }else{
                        Toast.makeText(API.mainActivity, "Error creando receta", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(API.mainActivity, "Error creando receta", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(API.mainActivity, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show()
        }
    }

    // Check if the recipe is valid
    fun isValid(): Boolean{
        return recipeName.value!!.isNotEmpty()
                && recipeDescription.value!!.isNotEmpty()
                && recipeMinutes.value!!.isNotEmpty()
    }

    // On change name
    fun onChangeName(name: String){
        _recipeName.value = name
    }

    // On change description
    fun onChangeDescription(description: String){
        // If the description is too long we dont allow it
        if(description.length > 75){
            return
        }

        _recipeDescription.value = description
    }

    // On change minutes
    fun onChangeMinutes(minutes: String, type: String){
        var aux = minutes

        // If it contains . or , we remove it
        if (minutes.contains(".") || minutes.contains(",")){
            println("Contains . or ,")
            aux = minutes.replace(".", "").replace(",", "")
        }

        // If type in hours, convert to minutes
        if (type == "H"){
            _recipeMinutes.value = (aux.toInt() * 60).toString()
        }

        _recipeMinutes.value = aux
    }

    // Add ingredient
    fun addIngredient(){
        val list = _ingredients.value?.toMutableList() ?: mutableListOf()
        list.add(Ingredient("",""))
        _ingredients.value = list
    }

    // Remove ingredient
    fun removeIngredient(index: Int){
        val list = _ingredients.value?.toMutableList() ?: mutableListOf()
        list.removeAt(index)
        _ingredients.value = list
    }

    // On change ingredient
    fun onChangeIngredient(index: Int, name: String = "", quantity: String = ""){
        val list = _ingredients.value?.toMutableList() ?: mutableListOf()
        list[index] = Ingredient(name, quantity)
        _ingredients.value = list
    }

    // Add instruction
    fun addInstruction(){
        val list = _instructions.value?.toMutableList() ?: mutableListOf()
        list.add("")
        _instructions.value = list
    }

    // Remove instruction
    fun removeInstruction(index: Int){
        val list = _instructions.value?.toMutableList() ?: mutableListOf()
        list.removeAt(index)
        _instructions.value = list
    }

    // On change instruction
    fun onChangeInstruction(index: Int, instruction: String){
        val list = _instructions.value?.toMutableList() ?: mutableListOf()
        list[index] = instruction
        _instructions.value = list
    }

    // Request Image from gallery
    fun requestImage(){
        //TODO
    }

    // Rquest Image from camera
    fun requestCamera(){
        //TODO
    }
}