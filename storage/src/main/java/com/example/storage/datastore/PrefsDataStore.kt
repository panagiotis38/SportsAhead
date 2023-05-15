package com.example.storage.datastore

interface PrefsDataStore {

    suspend fun <T> readObject(
        key: String,
        classType: Class<T>
    ): T?

    suspend fun <T> writeObject(key: String, value: T)

    suspend fun clear(key: String)
    suspend fun clearAll()

}