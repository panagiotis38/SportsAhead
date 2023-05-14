package com.example.sportsahead.di.modules

import android.app.Activity
import com.example.sportsahead.di.annotations.PerActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    @PerActivity
    fun provideActivity(): Activity {
        return activity
    }

}