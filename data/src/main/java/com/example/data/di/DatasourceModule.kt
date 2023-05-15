package com.example.data.di

import com.example.data.datasource.UpcomingEventsDatasource
import com.example.data.mapper.UpcomingEventsDataMapper
import com.example.network.di.NetworkModule
import com.example.network.repo.UpcomingEventsNetworkRepo
import com.example.storage.datastore.PrefsDataStore
import com.example.storage.di.StorageModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        NetworkModule::class,
        StorageModule::class
    ]
)
class DatasourceModule {

    @Provides
    @Singleton
    fun provideUpcomingEventsDatasource(
        networkRepo: UpcomingEventsNetworkRepo,
        dataStore: PrefsDataStore,
        dataMapper: UpcomingEventsDataMapper
    ): UpcomingEventsDatasource {
        return UpcomingEventsDatasource(networkRepo, dataStore, dataMapper)
    }

}