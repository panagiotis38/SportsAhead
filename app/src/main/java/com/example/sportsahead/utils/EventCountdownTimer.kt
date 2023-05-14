package com.example.sportsahead.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class EventCountdownTimer {

    suspend inline fun startTimer(
        timeInSeconds: Long,
        intervalInMillis: Long,
        crossinline onTick: (Long) -> Unit,
        crossinline onFinish: () -> Unit
    ) {

        (1..timeInSeconds)
            .asSequence()
            .asFlow()
            .onEach {
                delay(intervalInMillis)
                onTick.invoke(it)
            }
            .onCompletion {
                onFinish.invoke()
            }
            .cancellable()
            .collect()
    }

}