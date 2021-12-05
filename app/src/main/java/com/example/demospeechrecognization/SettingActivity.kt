package com.example.demospeechrecognization

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.demospeechrecognization.utils.BaseActivity
import com.example.demospeechrecognization.utils.SharedPref
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val sharedPresent=getSharedPreferences("setting", Context.MODE_PRIVATE)
        val volumeUpWake=sharedPresent.getInt("volume_wake",0)

        if(volumeUpWake!=1){
            switch_volume_wake.isChecked=true
        }
        switch_theme.isChecked=sharedPref.state


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
                sharedPref.state = true
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            } else {
                sharedPref.state = false
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            }
        }

    }

}
