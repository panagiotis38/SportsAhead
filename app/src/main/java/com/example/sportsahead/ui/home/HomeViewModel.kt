package com.example.sportsahead.ui.home

import androidx.lifecycle.viewModelScope
import com.example.data.datasource.UpcomingEventsDatasource
import com.example.sportsahead.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val datasource: UpcomingEventsDatasource,
) : BaseViewModel() {

    fun testDataSource() = viewModelScope.launch {
        val data = datasource.fetchUpcomingEvents()
        Logger.getGlobal().log(Level.INFO,"TestData: data = $data")
    }
}