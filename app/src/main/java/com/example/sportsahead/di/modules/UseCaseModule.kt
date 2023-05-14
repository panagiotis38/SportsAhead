package com.example.sportsahead.di.modules

import com.example.sportsahead.utils.ResourcesRepo
import com.example.sportsahead.utils.ResourcesRepoImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCaseModule {

    @Binds
    fun bindResourceRepo(resourcesRepoImpl: ResourcesRepoImpl): ResourcesRepo

}