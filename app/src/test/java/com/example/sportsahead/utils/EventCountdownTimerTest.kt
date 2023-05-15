package com.example.sportsahead.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
internal class EventCountdownTimerTest {

    private val timer = EventCountdownTimer()

    @Mock
    private lateinit var onTick: (Long) -> Unit

    @Mock
    private lateinit var onFinish: () -> Unit

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `when startTimer is called then tick events are emitted correctly`() =
        runTest {

            timer.startTimer(
                timeInSeconds = 3,
                intervalInMillis = 0,
                onTick = onTick,
                onFinish = {}
            )

            argumentCaptor<Long>().apply {
                verify(onTick, times(3)).invoke(capture())

                Assert.assertEquals(3, allValues.size)

                Assert.assertEquals(1, allValues[0])
                Assert.assertEquals(2, allValues[1])
                Assert.assertEquals(3, allValues[2])
            }

        }

    @Test
    fun `when timer finishes then finish callback is triggered`() =
        runTest {
            timer.startTimer(
                timeInSeconds = 3,
                intervalInMillis = 0,
                onTick = {},
                onFinish = onFinish
            )

            Mockito.verify(onFinish, times(1)).invoke()
        }

}