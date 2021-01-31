package com.example.demospeechrecognization.utils

import android.content.Context

class SharedPref(val context: Context) {

    val SESSION="SESSION"

    val pref=context.getSharedPreferences("MAIN",Context.MODE_PRIVATE)

    fun getString(key:String):String{
        return pref.getString(key,"")?:""
    }

    fun setString(key: String,value:String){
        pref.edit().putString(key,value).apply()
    }

}