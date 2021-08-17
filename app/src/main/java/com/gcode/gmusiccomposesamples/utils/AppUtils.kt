package com.gcode.gmusiccomposesamples.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class AppUtils:Application() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}