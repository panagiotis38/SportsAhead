package com.example.storage.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.firstOrNull

class PrefsDataStoreImpl(private val dataStore: DataStore<Preferences>) : PrefsDataStore {

    override suspend fun <T> readObject(key: String, classType: Class<T>): T? {
        return readString(key)?.let {
            try {
                Gson().fromJson(
                    it,
                    classType
                )
            } catch (e: JsonSyntaxException) {
                null
            }
        }
    }

    override suspend fun <T> writeObject(key: String, value: T) {
        writeString(
            key = key,
            value = Gson().toJson(value)
        )
    }

    override suspend fun clear(key: String) {
        dataStore.edit { pref ->
            if (pref.contains(stringPreferencesKey(key))) {
                pref.remove(stringPreferencesKey(key))
            }
        }
    }

    override suspend fun clearAll() {
        dataStore.edit { pref ->
            pref.clear()
        }
    }

    private suspend fun readString(key: String): String? {
        return dataStore.data.handleIOException().firstOrNull()?.get(stringPreferencesKey(key))
    }

    private suspend fun writeString(key: String, value: String) {
        dataStore.edit { pref ->
            pref[stringPreferencesKey(key)] = value
        }
    }

}