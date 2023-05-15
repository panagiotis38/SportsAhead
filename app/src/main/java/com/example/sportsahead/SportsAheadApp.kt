package com.example.sportsahead

import android.app.Application
import com.example.sportsahead.di.components.ApplicationComponent
import com.example.sportsahead.di.components.DaggerApplicationComponent

class SportsAheadApp : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationContext(this)
            .build()
    }

}