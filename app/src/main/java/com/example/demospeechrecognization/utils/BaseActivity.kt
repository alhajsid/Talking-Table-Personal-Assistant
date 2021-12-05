package com.example.demospeechrecognization.utils

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {

    lateinit var loadingDialog: ProgressDialog
    lateinit var sharedPref: SharedPref
    var isInternetAvailable=false
    var isLightTheme=true
    var firstTime=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = SharedPref(this)

        ConnectionLiveData(this).observe(this, Observer<Boolean> { t ->
            isInternetAvailable=t!!
            if (!firstTime) {
                val string = if (t) "Internet back" else "Internet Lost"
                Snackbar.make(window.decorView.rootView, string, Snackbar.LENGTH_LONG)
                        .setAction("CLOSE") { }
                        .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
                        .show()
            }
            firstTime=false
        })

        loadingDialog = ProgressDialog(this)
        loadingDialog.setTitle("Loading")
    }

    override fun onResume() {
        super.onResume()
        firstTime=true
    }

    override fun setContentView(layoutResID: Int) {
        isLightTheme=sharedPref.state
        if (isLightTheme) {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        } else {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        }
        super.setContentView(layoutResID)
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun startActivity(cls : Class<*>?) {
        val intent= Intent(this,cls)
        super.startActivity(intent)
    }


    fun showLoadingDialog() {
        loadingDialog.show()
    }

    fun showLoadingDialog(title: String) {
        loadingDialog.setTitle(title)
        showLoadingDialog()
    }

    fun hideLoadingDialog() {
        if(this::loadingDialog.isInitialized && loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (sharedPref.state != isLightTheme){
            isLightTheme = sharedPref.state
            if (isLightTheme) {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            } else {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            }
        }
    }
}