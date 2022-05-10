package com.kashonkov.videoapplication

import android.app.Application
import android.content.Context

class VideoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object{
        lateinit var appContext: Context
    }
}