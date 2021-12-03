package com.demo.phonepopup.integration_layer.utility

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.demo.phonepopup.integration_layer.sharedprefernce.SharedPreferenceManager

class MyApplication: Application() {
    var context: Context? = null;
    companion object {
        var mInstance: MyApplication? = null
    }

    override fun onCreate() {
        super.onCreate()


        mInstance = this
        mInstance = MyApplication()
        this.context = applicationContext

        val sharedPreferenceManager = SharedPreferenceManager(applicationContext)
        SharedPreferenceManager.initializeInstance(applicationContext)


        // To avoid dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }









}