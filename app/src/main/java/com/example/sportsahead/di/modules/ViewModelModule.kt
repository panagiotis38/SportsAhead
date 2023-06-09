package com.example.sportsahead.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sportsahead.di.ViewModelFactory
import com.example.sportsahead.di.annotations.ViewModelKey
import com.example.sportsahead.ui.dashboard.DashboardViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    fun bindExpandableViewModel(viewModel: DashboardViewModel): ViewModel

}