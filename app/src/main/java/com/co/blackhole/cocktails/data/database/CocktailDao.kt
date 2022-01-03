package com.co.blackhole.cocktails.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.co.blackhole.cocktails.data.model.Cocktail
import com.co.blackhole.cocktails.data.model.CocktailResponse

@Dao
interface CocktailDao {

    @Insert(entity = Cocktail::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAllCocktail(cocktail: Cocktail)

    @Query("SELECT * FROM cocktail_table ORDER BY name ASC")
    fun getCocktails(): LiveData<MutableList<Cocktail>>
}