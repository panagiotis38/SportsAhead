package com.example.data.di

import com.example.data.mapper.UpcomingEventsDataMapper
import com.example.storage.datastore.PrefsDataStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MapperModule {

    @Provides
    @Singleton
    fun provideUpcomingEventsDataMapper(dataStore: PrefsDataStore): UpcomingEventsDataMapper {
        return UpcomingEventsDataMapper(dataStore)
    }

}