package com.example.shopit.data.preferences

import android.content.Context
import android.util.Log
import com.example.shopit.data.cart.CartProductDataClass
import com.google.gson.Gson

class Preferences {
    object Singleton {

        private val PREF_NAME = "shop_it_preferences"

        //Key values
        val KEY_SHOPPING_CART = "shopping_cart"
        val KEY_FAVOURITE_STORES = "favourite_stores"

    //    val DESCRIPTIVE_SAMPLE_NAME = "concise_key_string"

        fun addString(header: String, data: String, context: Context): Boolean {
            val preferences = context.getSharedPreferences(header, Context.MODE_PRIVATE)
            preferences.edit().putString(header, data).apply()

            return (getString(header, context) == data)
        }

        fun addBoolean(header: String, data: Boolean, context: Context): Boolean {
            val preferences = context.getSharedPreferences(header, Context.MODE_PRIVATE)
            preferences.edit().putBoolean(header, data).apply()

            return (getBoolean(header, context) == data)
        }

        fun getBoolean(header: String, context: Context): Boolean {
            val preferences = context.getSharedPreferences(header, Context.MODE_PRIVATE)

            return preferences.getBoolean(header, false) // get the data that matches header
        }

        //gets data from SharedPrefs
        fun getString(header: String, context: Context): String? {

            val preferences = context.getSharedPreferences(header, Context.MODE_PRIVATE)
            try {
                val data = preferences.getString(header, null) // get the data that matches header
                if (data != null && data.isNotBlank()) {
                    return data
                }
                return data
            } catch (ex: ClassCastException) {
                return null
            }
        }

        private fun checkHeaderExists(header: String, context: Context): Boolean {

            val preferences = context.getSharedPreferences(header, Context.MODE_PRIVATE)

            return try {
                preferences.getString(header, null) != null
            } catch (ex: ClassCastException) {
                Log.d(TAG, "Check Header Exists Exception", ex)
                false
            }
        }


        fun addItemToList(header: String, data: CartProductDataClass, context: Context): Boolean {
            Log.d(TAG, "Add Item To List")
            val preferences = context.getSharedPreferences(header, Context.MODE_PRIVATE)
            var cartList: MutableList<CartProductDataClass> = mutableListOf()
            val gson = Gson()

            val cart = getListContents(header, context)
            if (cart != null && cart.isNotEmpty()) {
                //cart already has items, append to list
                try {
                    val jsonText = preferences.getString(header, null)
                    //make into list, append new item to end and return true/ false for success.
                    cartList = gson.fromJson(jsonText, Array<CartProductDataClass>::class.java).toMutableList()
                    val originalCartSize = cartList.size
                    cartList.add(data)
                    val newCartSize = cartList.size

                    for(item in cartList){
                        Log.d(TAG, item.cartProductName)
                    }

                    val cartListGson = gson.toJson(cartList)
                    if (cartListGson != null) {
                        Log.d(TAG, "cart list gson good - add cart item")
                        preferences.edit().putString(header, cartListGson).apply()
                        return (originalCartSize < newCartSize)
                    }else{
                        Log.d(TAG, "cart list gson was null - add cart item")
                        return false
                    }

                }catch(ex: ClassCastException){
                    Log.d(TAG, "Class Cast Exception - add cart item")
                    return false
                }
            }else{ //cart is empty, create new list and add this item to it.
                Log.d(TAG, "cart list was empty")
                val cartItemList = mutableListOf<CartProductDataClass>()

                cartItemList.add(data)
                Log.d(TAG, cartItemList[0].cartProductName)
                return try {
                    val textList: List<CartProductDataClass> = ArrayList(cartItemList)
                    val cartListGson = gson.toJson(textList)
                    preferences.edit().putString(header, cartListGson).apply()

                    val cartContents = getListContents(header, context)
                    Log.d(TAG, "cart contains ${cartContents?.size} items")

                    cartContents != null && cartContents.isNotEmpty() // return boolean if not null and not empty

                }catch(ex: ClassCastException){
                    Log.d(TAG, "Class Cast Exception - add cart item")
                    false
                }
            }
        }

        fun removeItemFromList(header: String, idx: Int , context: Context): Boolean {

            val preferences = context.getSharedPreferences(header, Context.MODE_PRIVATE)
            var cartList: MutableList<CartProductDataClass> = mutableListOf()
            val gson = Gson()

            var originalCartSize = 0
            var newCartSize = 0

            val cart = getListContents(header, context)
            if (cart != null && cart.isNotEmpty()) {
                try {
                    val jsonText = preferences.getString(header, null)
                    //make into list, append new item to end and return true/ false for success.
                    cartList = gson.fromJson(jsonText, Array<CartProductDataClass>::class.java).toMutableList()

                    originalCartSize = cartList.size
                    cartList.removeAt(idx)
                    newCartSize = cartList.size

                    val cartListGson = gson.toJson(cartList)
                    preferences.edit().putString(header, cartListGson).apply()

                    return (originalCartSize > newCartSize)

                }catch(ex: ClassCastException){
                    return false
                }
            }else{
                return false
            }
        }

        fun getListContents(header: String, context: Context): MutableList<CartProductDataClass>? {
            val preferences = context.getSharedPreferences(header, Context.MODE_PRIVATE)
            val gson = Gson()
            var jsonText: String?
            val gsonList: MutableList<CartProductDataClass>

            return try {
                jsonText = preferences.getString(header, null)

                if (jsonText != null){
                    gsonList = gson.fromJson(jsonText, Array<CartProductDataClass>::class.java).toMutableList()
                    gsonList //return GSON list
                }else{
                    null
                }

            } catch (ex: ClassCastException) {
                null
            }
        }

        fun deleteData(header: String, context: Context): Boolean {

            val preferences = context.getSharedPreferences(header, Context.MODE_PRIVATE)
            preferences.edit().remove(header).apply()

            return !checkHeaderExists(header, context)
        }
    }

    companion object {
        private const val TAG = "ShopIt-Preferences"
    }
}