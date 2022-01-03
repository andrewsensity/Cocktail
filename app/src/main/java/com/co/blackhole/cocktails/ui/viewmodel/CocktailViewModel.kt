package com.co.blackhole.cocktails.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.co.blackhole.cocktails.data.database.CocktailDatabase
import com.co.blackhole.cocktails.data.database.CocktailRepository
import com.co.blackhole.cocktails.data.model.Cocktail
import com.co.blackhole.cocktails.data.model.CocktailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CocktailViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<MutableList<Cocktail>>
    private val repository: CocktailRepository

    init {
        val cocktailDao = CocktailDatabase.getDatabase(application).cocktailDao()
        repository = CocktailRepository(cocktailDao)
        readAllData = repository.readAllData
    }

    fun addCocktail(cocktail: Cocktail) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCocktail(cocktail)
        }
    }
}