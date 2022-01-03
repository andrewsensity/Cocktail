package com.co.blackhole.cocktails.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cocktail_table")
data class Cocktail(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @SerializedName("strDrink") val name: String,
    @SerializedName("strDrinkThumb") val image: String,
    var price: Int
)