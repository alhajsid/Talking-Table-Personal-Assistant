package com.example.demospeechrecognization

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.demospeechrecognization.utils.CustomAppCompatActivity
import com.example.demospeechrecognization.utils.SharedPref
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : CustomAppCompatActivity() {

    lateinit var initApplicatio: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val sharedPresent=getSharedPreferences("setting", Context.MODE_PRIVATE)
        val volumeUpWake=sharedPresent.getInt("volume_wake",0)
        val theme=sharedPresent.getInt("theme",0)
        initApplicatio = SharedPref(this)

        if(volumeUpWake==1){
            switch_volume_wake.isChecked=true
        }
        switch_theme.isChecked=initApplicatio.state


        switch_volume_wake.setOnCheckedChangeListener { _, b ->
            if (b) {
                sharedPresent.edit()
                    .putInt("volume_wake", 1).apply()
            } else {
                sharedPresent.edit().putInt("volume_wake", 0).apply()
            }
        }

        switch_theme.setOnCheckedChangeListener { _, b ->

            if (b) {
                initApplicatio.state = true
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            } else {
                initApplicatio.state = false
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            }
        }

    }

}
