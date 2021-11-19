package com.example.demospeechrecognization.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPref(val context: Context) {

    val pref: SharedPreferences = context.getSharedPreferences("MAIN", Context.MODE_PRIVATE)

    var uid: String
        get() {
            return pref.getString("uid", "") ?:""
        }
        set(value) {
            val editor = pref.edit()
            editor.putString("uid", value)
            editor.apply()
        }


    var state: Boolean
        get() = pref.getBoolean("THEME_MODE", true)
        set(bVal) {
            val editor = pref.edit()
            editor.putBoolean("THEME_MODE", bVal)
            editor.apply()
        }


}