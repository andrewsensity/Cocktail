package com.co.blackhole.cocktails.data

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {

    private val PREFS = "data"
    private val SHOP_ID = "shopID"

    private val storage: SharedPreferences = context.getSharedPreferences(PREFS, 0)

    var shopID: Int
        get() = storage.getInt(SHOP_ID, 0)
        set(value) = storage.edit().putInt(SHOP_ID, value).apply()

    fun wipe() {
        storage.edit().clear().apply()
    }
}