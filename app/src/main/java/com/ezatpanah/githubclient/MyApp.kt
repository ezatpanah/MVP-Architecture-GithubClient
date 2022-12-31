package com.ezatpanah.githubclient

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application(){
    private var instance: MyApp? = null

    override fun onCreate() {
        instance = this
        super.onCreate()
    }
    fun getContext(): Context {
        return instance!!.applicationContext
    }
}

