package com.co.blackhole.cocktails

import android.app.Application
import androidx.room.Room
import com.co.blackhole.cocktails.data.database.CocktailDatabase
import dagger.hilt.android.HiltAndroidApp

class CocktailApplication: Application() {

    val room = Room
        .databaseBuilder(this, CocktailDatabase::class.java, "cocktail")
        .build()
}