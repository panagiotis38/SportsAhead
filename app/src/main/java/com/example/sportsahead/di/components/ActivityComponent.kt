package com.example.sportsahead.di.components

import com.example.sportsahead.ui.base.BaseActivity
import com.example.sportsahead.di.annotations.PerActivity
import com.example.sportsahead.di.modules.ActivityModule
import com.example.sportsahead.di.modules.FragmentModule
import com.example.sportsahead.ui.home.HomeActivity
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(activity: BaseActivity)

    fun inject(activity: HomeActivity)

    fun fragmentComponent(fragmentModule: FragmentModule): FragmentComponent

}