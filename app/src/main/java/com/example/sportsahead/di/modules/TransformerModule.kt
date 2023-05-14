package com.example.sportsahead.di.modules

import com.example.sportsahead.ui.dashboard.transformer.UpcomingEventsUiTransformer
import com.example.sportsahead.ui.dashboard.transformer.UpcomingEventsUiTransformerImpl
import dagger.Binds
import dagger.Module

@Module
interface TransformerModule {

    @Binds
    fun bindUpcomingEventsUiTransformer(uiTransformerImpl: UpcomingEventsUiTransformerImpl): UpcomingEventsUiTransformer

}