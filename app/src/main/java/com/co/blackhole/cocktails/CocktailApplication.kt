package com.co.blackhole.cocktails

import android.app.Application
import androidx.room.Room
import com.co.blackhole.cocktails.data.Prefs
import com.co.blackhole.cocktails.data.database.CocktailDatabase
import dagger.hilt.android.HiltAndroidApp

class CocktailApplication: Application() {

    companion object {
        lateinit var prefs: Prefs
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
    }
}