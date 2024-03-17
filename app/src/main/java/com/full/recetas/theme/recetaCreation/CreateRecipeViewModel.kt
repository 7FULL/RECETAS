package com.full.recetas.theme.recetaCreation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.ETC1.encodeImage
import android.opengl.ETC1.isValid
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageProxy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.recetas.models.CameraState
import com.full.recetas.models.Ingredient
import com.full.recetas.models.Recipe
import com.full.recetas.navigation.AppScreens
import com.full.recetas.navigation.NavigationManager
import com.full.recetas.network.API
import com.full.recetas.utils.SavePhotoToGalleryUseCase
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

class CreateRecipeViewModel(private val savePhotoToGalleryUseCase: SavePhotoToGalleryUseCase, val recipeString: String): ViewModel() {

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

    private val _image = MutableLiveData<Bitmap>()
    val image: LiveData<Bitmap> = _image

    private val _imageSRC = MutableLiveData<String>()
    val imageSRC: LiveData<String> = _imageSRC

    private val _isEditing = MutableLiveData<Boolean>(false)
    val isEditing: LiveData<Boolean> = _isEditing

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    val storage = Firebase.storage

    private var _id = ""
    init {
        // We clear the fields
        _recipeName.value = ""
        _recipeDescription.value = ""
        _recipeMinutes.value = ""
        _selectedTags.value = listOf()
        _ingredients.value = listOf()
        _instructions.value = listOf()

        if (!API.isLogged){
            NavigationManager.instance!!.navigate(AppScreens.Login.route)
        }else{
            // Get tags
            getTags()

            // If we are editing a recipe
            if(recipeString.isNotEmpty()){
                val recipe = API.gson.fromJson(recipeString, Recipe::class.java)

                _recipeName.value = recipe.name
                _recipeDescription.value = recipe.description
                _recipeMinutes.value = recipe.minutes.toString()
                _selectedTags.value = recipe.tags
                _ingredients.value = recipe.ingredients
                _instructions.value = recipe.cookingInstructions

                _imageSRC.value = recipe.image

                _isEditing.value = true

                _id = recipe._id
            }
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
    fun createRecipe(selectedIndexes: List<Int>, image: Bitmap?){
        if(isValid()){
            val ingredientOptions = listOf("G", "KG", "ML", "L")

            // If any ingredient is empty
            for (i in _ingredients.value!!){
                if(i.name.isEmpty() || i.quantity.isEmpty()){
                    Toast.makeText(API.mainActivity, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show()
                    return
                }
            }

            // We add the selected quantity
            val ingredients = _ingredients.value?.toMutableList() ?: mutableListOf()

            for (i in ingredients.indices){
                ingredients[i] = ingredients[i].copy(quantity = ingredients[i].quantity + " " + ingredientOptions[selectedIndexes[i]])
            }

            if (image == null && _imageSRC.value?.isEmpty() != false){
                Toast.makeText(API.mainActivity, "Falta la imagen", Toast.LENGTH_SHORT).show()
                return
            }

            val recipe = Recipe(
                name = recipeName.value!!,
                publisher = API.User.value!!,
                description = recipeDescription.value!!,
                minutes = recipeMinutes.value!!.toInt(),
                tags = selectedTags.value!!,
                ingredients = ingredients,
                cookingInstructions = instructions.value?.toMutableList() ?: mutableListOf(),
            )

            // If we are editing a recipe
            if(recipeString.isNotEmpty()){
                recipe._id = _id
            }

            viewModelScope.launch {
                _isLoading.value = true

                val response = API.service.createRecipe(recipe, isEditing.value!!)

                if(response.isSuccessful){
                    val data = response.body()!!

                    if(data.code == 200){
                        // We put the image in the storage
                        if (image != null){
                            val stream = ByteArrayOutputStream()

                            image?.compress(Bitmap.CompressFormat.PNG, 100, stream)

                            val byteArray = stream.toByteArray()


                            val storageRef = storage.reference
                            val imageRef = storageRef.child("images/${data.data!!}.png")

                            imageRef.putBytes(byteArray).addOnSuccessListener {
                                //_isLoading.value = false

                                Toast.makeText(API.mainActivity, if (_isEditing.value == false) "Receta creada" else "Receta editada"  , Toast.LENGTH_SHORT).show()
                                NavigationManager.instance!!.navigate(AppScreens.HomeLogged.route)
                            }
                        }else{
                            //_isLoading.value = false

                            Toast.makeText(API.mainActivity, if (_isEditing.value == false) "Receta creada" else "Receta editada"  , Toast.LENGTH_SHORT).show()
                            NavigationManager.instance!!.navigate(AppScreens.HomeLogged.route)
                        }
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
        return recipeName.value?.isNotEmpty()?: false
                && recipeDescription.value?.isNotEmpty()?: false
                && recipeMinutes.value?.isNotEmpty()?: false
                && selectedTags.value?.isNotEmpty()?: false
                && ingredients.value?.isNotEmpty()?: false
                && instructions.value?.isNotEmpty()?: false
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

        var nameAux = name
        var quantityAux = quantity

        if(name.isEmpty()){
            nameAux = list[index].name
        }

        if(quantity.isEmpty()){
            quantityAux = list[index].quantity
        }

        list[index] = Ingredient(nameAux, quantityAux)
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

    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun storePhotoInGallery(bitmap: Bitmap) {
        viewModelScope.launch {
            savePhotoToGalleryUseCase.call(bitmap)
            updateCapturedPhotoState(bitmap)

            Toast.makeText(API.mainActivity, "Foto guardada correctamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateCapturedPhotoState(updatedPhoto: Bitmap?) {
        _state.value.capturedImage?.recycle()
        _state.value = _state.value.copy(capturedImage = updatedPhoto)
    }

    override fun onCleared() {
        _state.value.capturedImage?.recycle()
        super.onCleared()
    }
}