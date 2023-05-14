package com.example.sportsahead.ui.dashboard

import androidx.lifecycle.viewModelScope
import com.example.data.base.DataResponse
import com.example.data.datasource.UpcomingEventsDatasource
import com.example.sportsahead.ui.base.BaseViewModel
import com.example.sportsahead.ui.dashboard.transformer.UpcomingEventsUiTransformer
import com.example.sportsahead.ui.model.ErrorUiModel
import com.example.sportsahead.ui.model.dashboard.UpcomingEventsUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val datasource: UpcomingEventsDatasource,
    private val uiTransformer: UpcomingEventsUiTransformer
) : BaseViewModel() {

    private val mutableUiFlow = MutableStateFlow(DashboardUiState())
    val uiFlow = mutableUiFlow.asStateFlow()

    fun fetchUpcomingEvents() = viewModelScope.launch {
        when (val response = datasource.fetchUpcomingEvents()) {
            is DataResponse.Success -> {
                response.data?.let {
                    emitSuccessEvent(
                        uiModel = uiTransformer.transformToUiModel(dataModel = it)
                    )
                } ?: run {
                    emitErrorEvent(
                        title = "Empty data title",
                        message = "Empty data message"
                    )
                }
            }
            is DataResponse.Error -> {
                //TODO: handle error case here
                emitErrorEvent(
                    title = "Generic error title",
                    message = "Generic error message"
                )
            }
        }

    }

    fun onSectionClicked(sportId: String) {
        mutableUiFlow.update { state ->
            state.copy(
                isLoading = false,
                error = ErrorUiModel(),
                content = state.content.apply {
                    upcomingEvents.find {
                        it.id == sportId
                    }?.apply {
                        isExpanded.value = isExpanded.value.not()
                    }
                }
            )
        }
    }

    fun onFavouriteClicked(sportId: String, eventId: String) {
        mutableUiFlow.update { state ->
            state.copy(
                isLoading = false,
                error = ErrorUiModel(),
                content = state.content.apply {
                    upcomingEvents.find {
                        it.id == sportId
                    }?.events?.find { it.id == eventId }?.apply {
                        isFavourite.value = isFavourite.value.not()
                        persistToStorage(
                            eventId = eventId,
                            sportId = sportId,
                            isFavourite = isFavourite.value
                        )
                    }
                }
            )
        }
    }

    private fun emitSuccessEvent(uiModel: UpcomingEventsUiModel) {
        mutableUiFlow.update {
            it.copy(
                isLoading = false,
                error = ErrorUiModel(),
                content = uiModel
            )
        }
    }

    private fun emitErrorEvent(title: String, message: String) {
        mutableUiFlow.update {
            it.copy(
                isLoading = false,
                error = ErrorUiModel(
                    show = true,
                    title = title,
                    message = message,
                    onTryAgain = {
                        emitLoadingEvent()
                        fetchUpcomingEvents()
                    }
                ),
                content = UpcomingEventsUiModel()
            )
        }
    }

    private fun emitLoadingEvent() {
        mutableUiFlow.update {
            it.copy(
                isLoading = true,
                error = ErrorUiModel(),
                content = UpcomingEventsUiModel()
            )
        }
    }

    private fun persistToStorage(sportId: String, eventId: String, isFavourite: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            if (isFavourite) {
                datasource.persistFavourite(
                    eventId = eventId,
                    sportId = sportId
                )
            } else {
                datasource.removeFavourite(
                    eventId = eventId,
                    sportId = sportId
                )
            }
        }

}

data class DashboardUiState(
    val isLoading: Boolean = true,
    val content: UpcomingEventsUiModel = UpcomingEventsUiModel(),
    val error: ErrorUiModel = ErrorUiModel()
)