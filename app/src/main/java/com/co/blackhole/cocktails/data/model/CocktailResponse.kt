package com.co.blackhole.cocktails.data.model

import com.google.gson.annotations.SerializedName

data class CocktailResponse(
    @SerializedName("drinks") val cocktails: MutableList<Cocktail>
)