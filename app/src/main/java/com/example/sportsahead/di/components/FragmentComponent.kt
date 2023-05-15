package com.example.sportsahead.di.components

import com.example.sportsahead.di.annotations.PerFragment
import com.example.sportsahead.di.modules.FragmentModule
import com.example.sportsahead.ui.base.BaseFragment
import dagger.Subcomponent

@PerFragment
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(fragment: BaseFragment)
}