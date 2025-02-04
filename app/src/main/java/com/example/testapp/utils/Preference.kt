package com.example.testapp.utils

import android.content.Context
import android.content.SharedPreferences


class Preference private constructor(context: Context) {
    private val sharedPreference: SharedPreferences

    init {
        sharedPreference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private const val PREF_NAME = "App_Prefs"

        @Volatile
        private var instance: Preference? = null

        fun getInstance(context: Context): Preference {
            return instance ?: synchronized(this) {
                instance ?: Preference(context).also { instance = it }
            }
        }
    }

    fun getStringValue(key:String,defValue:String="null"):String{
        return sharedPreference.getString(key,"")?:defValue
    }

    fun saveStringValue(key:String,value:String){
        sharedPreference.edit().putString(key,value).apply()
    }
}