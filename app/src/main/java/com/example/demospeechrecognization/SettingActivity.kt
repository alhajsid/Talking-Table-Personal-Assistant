package com.example.demospeechrecognization

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val sharedPresent=getSharedPreferences("setting", Context.MODE_PRIVATE)
        val volumeUpWake=sharedPresent.getInt("volume_wake",0)
        val theme=sharedPresent.getInt("theme",0)
        if(volumeUpWake==1){
            switch_volume_wake.isChecked=true
        }
        if(theme==1){
            applyDarkTheme()
            switch_theme.isChecked=true
        }

        switch_volume_wake.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                val sharedPresent=getSharedPreferences("setting", Context.MODE_PRIVATE).edit()
                sharedPresent.putInt("volume_wake",1)
                sharedPresent.apply()
            }else{
                val sharedPresent=getSharedPreferences("setting", Context.MODE_PRIVATE).edit()
                sharedPresent.putInt("volume_wake",0)
                sharedPresent.apply()
            }
        }

        switch_theme.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                val sharedPresent=getSharedPreferences("setting", Context.MODE_PRIVATE).edit()
                sharedPresent.putInt("theme",1)
                sharedPresent.apply()
                applyDarkTheme()
            }else{
                val sharedPresent=getSharedPreferences("setting", Context.MODE_PRIVATE).edit()
                sharedPresent.putInt("theme",0)
                sharedPresent.apply()
                applyLightTheme()
            }
        }

    }

    fun applyDarkTheme(){
        setting_container.setBackgroundColor(Color.parseColor("#000000"))
        theme_container.setBackgroundColor(Color.parseColor("#000000"))
        volume_up_container.setBackgroundColor(Color.parseColor("#000000"))

        setting_main_container.setBackgroundColor(Color.parseColor("#212121"))

        tv_volume_up.setTextColor(Color.parseColor("#ffffff"))
        tv_setting.setTextColor(Color.parseColor("#ffffff"))
        tv_theme.setTextColor(Color.parseColor("#ffffff"))

        divider_1.setBackgroundColor(Color.parseColor("#70ffffff"))
        divider_2.setBackgroundColor(Color.parseColor("#70ffffff"))
        divider_3.setBackgroundColor(Color.parseColor("#70ffffff"))
        divider_4.setBackgroundColor(Color.parseColor("#70ffffff"))
    }

    fun applyLightTheme(){
        setting_container.setBackgroundColor(Color.parseColor("#ffffff"))
        theme_container.setBackgroundColor(Color.parseColor("#ffffff"))
        volume_up_container.setBackgroundColor(Color.parseColor("#ffffff"))

        setting_main_container.setBackgroundColor(Color.parseColor("#DFDFDF"))

        tv_volume_up.setTextColor(Color.parseColor("#000000"))
        tv_setting.setTextColor(Color.parseColor("#000000"))
        tv_theme.setTextColor(Color.parseColor("#000000"))

        divider_1.setBackgroundColor(Color.parseColor("#B1B1B1"))
        divider_2.setBackgroundColor(Color.parseColor("#B1B1B1"))
        divider_3.setBackgroundColor(Color.parseColor("#B1B1B1"))
        divider_4.setBackgroundColor(Color.parseColor("#B1B1B1"))
    }

}
