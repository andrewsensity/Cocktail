package com.co.blackhole.cocktails.data

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {

    private val PREFS = "data"
    private val COCKTAIL_NAME = "cocktailName"
    private val PRICE = "price"
    private val QUANTITY = "quantity"
    private val SHOP_ID = "shopID"

    private val storage: SharedPreferences = context.getSharedPreferences(PREFS, 0)

    var cocktailName: Int
        get() = storage.getInt(COCKTAIL_NAME, 0)
        set(value) = storage.edit().putInt(COCKTAIL_NAME, value).apply()

    var price: Int
        get() = storage.getInt(PRICE, 0)
        set(value) = storage.edit().putInt(PRICE, value).apply()

    var quantity: Int
        get() = storage.getInt(QUANTITY, 0)
        set(value) = storage.edit().putInt(QUANTITY, value).apply()

    var shopID: Int
        get() = storage.getInt(SHOP_ID, 0)
        set(value) = storage.edit().putInt(SHOP_ID, value).apply()

    fun wipe() {
        storage.edit().clear().apply()
    }
}