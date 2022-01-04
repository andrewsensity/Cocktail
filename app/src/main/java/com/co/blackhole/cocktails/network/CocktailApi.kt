package com.co.blackhole.cocktails.network

import com.co.blackhole.cocktails.data.model.CocktailResponse
import retrofit2.Call
import retrofit2.http.GET

interface CocktailApi {

//    @GET("filter.php?c=Cocktail")
    @GET("filter.php?c=Ordinary_Drink")
    fun getCocktails(): Call<CocktailResponse>
}