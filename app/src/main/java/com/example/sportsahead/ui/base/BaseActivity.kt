package com.example.sportsahead.ui.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sportsahead.di.components.ActivityComponent
import com.example.sportsahead.di.modules.ActivityModule
import com.example.sportsahead.SportsAheadApp
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivityComponent()
    }

    private fun initActivityComponent() {
        activityComponent =
            (application as SportsAheadApp)
                .applicationComponent
                .activityComponent(ActivityModule(this))
        activityComponent.inject(this)
    }
}