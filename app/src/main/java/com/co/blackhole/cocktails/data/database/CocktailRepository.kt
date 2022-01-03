package com.co.blackhole.cocktails.data.database

import androidx.lifecycle.LiveData
import com.co.blackhole.cocktails.data.model.Cocktail

class CocktailRepository(private val cocktailDao: CocktailDao) {

    val readAllData: LiveData<MutableList<Cocktail>> = cocktailDao.getCocktails()

    suspend fun addCocktail(cocktail: Cocktail) {
        cocktailDao.addAllCocktail(cocktail)
    }
}