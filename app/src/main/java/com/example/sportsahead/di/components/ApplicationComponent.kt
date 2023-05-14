package com.example.sportsahead.di.components

import android.app.Application
import com.example.sportsahead.di.ViewModelFactory
import com.example.sportsahead.di.modules.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        ViewModelModule::class,
        TransformerModule::class,
        UseCaseModule::class
    ]
)
interface ApplicationComponent {

    fun application(): Application
    fun viewModelFactory(): ViewModelFactory

    fun activityComponent(activityModule: ActivityModule): ActivityComponent

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent

        @BindsInstance
        fun applicationContext(applicationContext: Application): Builder
    }
}