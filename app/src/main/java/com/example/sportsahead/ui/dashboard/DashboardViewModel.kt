package com.example.sportsahead.ui.dashboard

import androidx.lifecycle.viewModelScope
import com.example.data.base.DataResponse
import com.example.data.datasource.UpcomingEventsDatasource
import com.example.data.mapper.UpcomingEventsDataMapper.Companion.ONE_SECOND_IN_MILLIS
import com.example.data.model.UpcomingEventsModel
import com.example.sportsahead.R
import com.example.sportsahead.ui.base.BaseViewModel
import com.example.sportsahead.ui.dashboard.transformer.UpcomingEventsUiTransformer
import com.example.sportsahead.ui.model.ErrorUiModel
import com.example.sportsahead.ui.model.dashboard.UpcomingEventsUiModel
import com.example.sportsahead.utils.EventCountdownTimer
import com.example.sportsahead.utils.ResourcesRepo
import com.example.sportsahead.utils.formatDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val resourcesRepo: ResourcesRepo,
    private val datasource: UpcomingEventsDatasource,
    private val uiTransformer: UpcomingEventsUiTransformer
) : BaseViewModel() {

    private lateinit var simpleDateFormat: SimpleDateFormat

    private val mutableUiFlow = MutableStateFlow(DashboardUiState())
    val uiFlow = mutableUiFlow.asStateFlow()

    fun fetchUpcomingEvents() = viewModelScope.launch {
        when (val response = datasource.fetchUpcomingEvents()) {
            is DataResponse.Success -> {
                response.data?.let {
                    emitSuccessEvent(
                        uiModel = uiTransformer.transformToUiModel(dataModel = it)
                    )
                    startEventTimer(it)
                } ?: run {
                    emitErrorEvent(
                        title = resourcesRepo.getString(R.string.generic_error_title),
                        message = resourcesRepo.getString(R.string.generic_error_message)
                    )
                }
            }
            is DataResponse.Error -> {
                emitErrorEvent(
                    title = resourcesRepo.getString(R.string.generic_error_title),
                    message = resourcesRepo.getString(R.string.generic_error_message)
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

    private fun startEventTimer(dataModel: UpcomingEventsModel) = viewModelScope.launch {
        initSimpleDateFormat()

        EventCountdownTimer().startTimer(
            timeInSeconds = (dataModel.latestEventStartTimeInMillis / ONE_SECOND_IN_MILLIS),
            intervalInMillis = ONE_SECOND_IN_MILLIS,
            onTick = {
                emitTimerTickEvent()
            },
            onFinish = {
                // do nothing
            }
        )
    }

    private fun initSimpleDateFormat() {
        simpleDateFormat = SimpleDateFormat(
            EVENTS_START_TIME_PATTERN,
            Locale.getDefault()
        ).also {
            it.timeZone = TimeZone.getTimeZone(TIMEZONE_UTC)
        }
    }

    private fun emitTimerTickEvent() {
        val currentContent = mutableUiFlow.value.content
        val newContent = currentContent.apply {
            upcomingEvents.forEach { sport ->
                sport.events.forEach { event ->
                    event.millisUntilStart?.apply {
                        event.millisUntilStart = (this - ONE_SECOND_IN_MILLIS)
                    }?.also {
                        if (it <= 0) {
                            event.millisUntilStart = 0
                        }
                    }
                    event.formattedTimeUntilStart.value =
                        event.millisUntilStart?.formatDate(simpleDateFormat).orEmpty()
                }
            }
        }
        mutableUiFlow.update {
            it.copy(
                isLoading = false,
                error = ErrorUiModel(),
                content = newContent
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
                    buttonText = resourcesRepo.getString(R.string.error_button_text),
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

    companion object {
        private const val EVENTS_START_TIME_PATTERN = "HH:mm:ss"
        private const val TIMEZONE_UTC = "UTC"
    }

}

data class DashboardUiState(
    val isLoading: Boolean = true,
    val content: UpcomingEventsUiModel = UpcomingEventsUiModel(),
    val error: ErrorUiModel = ErrorUiModel()
)