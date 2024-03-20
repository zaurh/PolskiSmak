package com.zaurh.polskismak.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zaurh.polskismak.data.remote.dto.Ingredient
import com.zaurh.polskismak.data.remote.dto.Instructions
import com.zaurh.polskismak.data.remote.dto.MealsItem

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun toInstructionsJson(instructions: List<Instructions>) : String {
        return Gson().toJson(
            instructions,
            object : TypeToken<ArrayList<Instructions>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromInstructionsJson(json: String): List<Instructions>{
        return Gson().fromJson<ArrayList<Instructions>>(
            json,
            object: TypeToken<ArrayList<Instructions>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toIngredientsJson(ingredients: List<Ingredient>) : String {
        return Gson().toJson(
            ingredients,
            object : TypeToken<ArrayList<Ingredient>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromIngredientsJson(json: String): List<Ingredient>{
        return Gson().fromJson<ArrayList<Ingredient>>(
            json,
            object: TypeToken<ArrayList<Ingredient>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toTagsJson(tags: List<String>) : String {
        return Gson().toJson(
            tags,
            object : TypeToken<ArrayList<Ingredient>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromTagsJson(json: String): List<String>{
        return Gson().fromJson<ArrayList<String>>(
            json,
            object: TypeToken<ArrayList<String>>(){}.type
        ) ?: emptyList()
    }

}