package com.saxipapsi.saxi_movie

import android.app.Application
import com.saxipapsi.saxi_movie.di.KoinModules

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinModules.init(this)
    }
}