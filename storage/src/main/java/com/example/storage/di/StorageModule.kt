package com.example.storage.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.storage.datastore.DataStoreConstants.PREFS_DATASTORE_INSTANCE_NAME
import com.example.storage.datastore.PrefsDataStore
import com.example.storage.datastore.PrefsDataStoreImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Provides
    @Singleton
    fun providePrefsDataStoreInstance(application: Application): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { application.preferencesDataStoreFile(PREFS_DATASTORE_INSTANCE_NAME) }
        )
    }

    @Provides
    @Singleton
    fun providePrefsDataStore(dataStoreInstance: DataStore<Preferences>): PrefsDataStore {
        return PrefsDataStoreImpl(dataStoreInstance)
    }

}