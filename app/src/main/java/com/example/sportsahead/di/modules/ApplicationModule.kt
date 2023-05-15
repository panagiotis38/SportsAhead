package com.example.sportsahead.di.modules

import com.example.data.di.DatasourceModule
import com.example.data.di.MapperModule
import dagger.Module

@Module(
    includes = [
        DatasourceModule::class,
        MapperModule::class
    ]
)
class ApplicationModule