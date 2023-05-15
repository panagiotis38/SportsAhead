package com.example.sportsahead.ui.dashboard

import androidx.compose.runtime.mutableStateOf
import com.example.data.base.DataResponse
import com.example.data.datasource.UpcomingEventsDatasource
import com.example.data.model.UpcomingEventsModel
import com.example.sportsahead.R
import com.example.sportsahead.ui.base.BaseViewModelTest
import com.example.sportsahead.ui.dashboard.transformer.UpcomingEventsUiTransformer
import com.example.sportsahead.ui.model.dashboard.EventUiModel
import com.example.sportsahead.ui.model.dashboard.SportUiModel
import com.example.sportsahead.ui.model.dashboard.UpcomingEventsUiModel
import com.example.sportsahead.utils.ResourcesRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
internal class DashboardViewModelTest : BaseViewModelTest() {

    @Mock
    private lateinit var resourcesRepo: ResourcesRepo

    @Mock
    private lateinit var uiTransformer: UpcomingEventsUiTransformer

    @Mock
    private lateinit var datasource: UpcomingEventsDatasource

    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        viewModel = DashboardViewModel(
            datasource = datasource,
            uiTransformer = uiTransformer,
            resourcesRepo = resourcesRepo
        )
    }

    @Test
    fun `given successful data response when fetchUpcomingEvents is called then flow is correct`() =
        runTest {
            Mockito.`when`(datasource.fetchUpcomingEvents()).thenReturn(
                DataResponse.Success(
                    data = UpcomingEventsModel(
                        upcomingEvents = listOf(), latestEventStartTimeInMillis = 1000L
                    )
                )
            )
            Mockito.`when`(uiTransformer.transformToUiModel(any()))
                .thenReturn(uiModel())

            viewModel.fetchUpcomingEvents()

            with(viewModel.uiFlow.value) {
                Assert.assertFalse(error.show)
                Assert.assertFalse(isLoading)
                Assert.assertTrue(content.upcomingEvents.size == 2)
                Assert.assertTrue(content.upcomingEvents.first().id == "1000")
                Assert.assertTrue(content.upcomingEvents.first().name == "Sport 1000")
                Assert.assertTrue(content.upcomingEvents.first().events.size == 3)
                Assert.assertTrue(content.upcomingEvents.first().events.first().id == "1001")
                Assert.assertTrue(content.upcomingEvents.first().events.first().firstContestant == "First contestant of event 1001")
                Assert.assertTrue(content.upcomingEvents.first().events.first().secondContestant == "Second contestant of event 1001")
                Assert.assertTrue(content.upcomingEvents.first().events.first().millisUntilStart == (10000L + 1001L))
                Assert.assertTrue(content.upcomingEvents.first().events.first().isFavourite.value)
                Assert.assertTrue(content.upcomingEvents.first().events.first().formattedTimeUntilStart.value == "00:00:38")
            }
        }

    @Test
    fun `given data response with null data when fetchUpcomingEvents is called then flow is correct`() =
        runTest {
            Mockito.`when`(datasource.fetchUpcomingEvents()).thenReturn(
                DataResponse.Success(
                    data = null
                )
            )
            Mockito.`when`(resourcesRepo.getString(R.string.generic_error_title))
                .thenReturn("Generic error title")
            Mockito.`when`(resourcesRepo.getString(R.string.generic_error_message))
                .thenReturn("Generic error message")
            Mockito.`when`(resourcesRepo.getString(R.string.error_button_text))
                .thenReturn("Try again")

            viewModel.fetchUpcomingEvents()

            with(viewModel.uiFlow.value) {
                Assert.assertFalse(isLoading)
                Assert.assertTrue(content.upcomingEvents.isEmpty())
                Assert.assertTrue(error.show)
                Assert.assertTrue(error.title == "Generic error title")
                Assert.assertTrue(error.message == "Generic error message")
                Assert.assertTrue(error.buttonText == "Try again")
            }
        }

    @Test
    fun `given error data response when fetchUpcomingEvents is called then flow is correct`() =
        runTest {
            Mockito.`when`(datasource.fetchUpcomingEvents()).thenReturn(
                DataResponse.Error(code = "400", message = "error")
            )
            Mockito.`when`(resourcesRepo.getString(R.string.generic_error_title))
                .thenReturn("Generic error title")
            Mockito.`when`(resourcesRepo.getString(R.string.generic_error_message))
                .thenReturn("Generic error message")
            Mockito.`when`(resourcesRepo.getString(R.string.error_button_text))
                .thenReturn("Try again")

            viewModel.fetchUpcomingEvents()

            with(viewModel.uiFlow.value) {
                Assert.assertFalse(isLoading)
                Assert.assertTrue(content.upcomingEvents.isEmpty())
                Assert.assertTrue(error.show)
                Assert.assertTrue(error.title == "Generic error title")
                Assert.assertTrue(error.message == "Generic error message")
                Assert.assertTrue(error.buttonText == "Try again")
            }
        }

    @Test
    fun `when onSectionClicked is called then the given sport's expand state changes`() = runTest {
        Mockito.`when`(datasource.fetchUpcomingEvents()).thenReturn(
            DataResponse.Success(
                data = UpcomingEventsModel(
                    upcomingEvents = listOf(), latestEventStartTimeInMillis = 1000L
                )
            )
        )
        Mockito.`when`(uiTransformer.transformToUiModel(any()))
            .thenReturn(uiModel())
        viewModel.fetchUpcomingEvents()

        viewModel.onSectionClicked("1000")
        viewModel.onSectionClicked("2000")

        with(viewModel.uiFlow.value) {
            Assert.assertFalse(isLoading)
            Assert.assertFalse(error.show)
            Assert.assertFalse(content.upcomingEvents.first().isExpanded.value)
            Assert.assertTrue(content.upcomingEvents[1].isExpanded.value)
        }
    }

    @Test
    fun `when onFavouriteClicked is called then the given event's favourite state changes`() = runTest {
        Mockito.`when`(datasource.fetchUpcomingEvents()).thenReturn(
            DataResponse.Success(
                data = UpcomingEventsModel(
                    upcomingEvents = listOf(), latestEventStartTimeInMillis = 1000L
                )
            )
        )
        Mockito.`when`(uiTransformer.transformToUiModel(any()))
            .thenReturn(uiModel())
        viewModel.fetchUpcomingEvents()

        viewModel.onFavouriteClicked(sportId = "1000", eventId = "1001")
        viewModel.onFavouriteClicked(sportId = "1000", eventId = "1002")

        with(viewModel.uiFlow.value.content.upcomingEvents.first().events) {
            Assert.assertFalse(first().isFavourite.value)
            Assert.assertTrue(get(1).isFavourite.value)
        }
    }

    private fun uiModel() = UpcomingEventsUiModel(
        upcomingEvents = listOf(
            sportModel(id =1000L, isExpanded = true),
            sportModel(id = 2000L, isExpanded = false),
        )
    )

    private fun sportModel(id: Long, isExpanded: Boolean) = SportUiModel(
        id = id.toString(),
        name = "Sport $id",
        events = listOf(
            eventModel(id = id + 1L, favourite = true),
            eventModel(id = id + 2L, favourite = false),
            eventModel(id = id + 3L, favourite = false)
        ),
        isExpanded = mutableStateOf(isExpanded)
    )

    private fun eventModel(id: Long, favourite: Boolean) = EventUiModel(
        id = id.toString(),
        firstContestant = "First contestant of event $id",
        secondContestant = "Second contestant of event $id",
        millisUntilStart = 10000L + id,
        isFavourite = mutableStateOf(favourite),
        formattedTimeUntilStart = mutableStateOf("00:00:38")
    )

}