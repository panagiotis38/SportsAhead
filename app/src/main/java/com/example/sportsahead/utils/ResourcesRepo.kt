package com.example.sportsahead.utils

import android.app.Application
import javax.inject.Inject
import javax.inject.Singleton

interface ResourcesRepo {

    fun getString(id: Int): String

}

@Singleton
class ResourcesRepoImpl @Inject constructor(
    private val application: Application
) : ResourcesRepo {

    override fun getString(id: Int): String {
        return application.getString(id)
    }

}