package com.example.network.interceptors

import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Loggable
import java.util.logging.Level
import java.util.logging.Logger

class CurlLoggerInterceptor(loggable: Loggable) : CurlInterceptor(loggable)

class CurlLogger(private val level: Level) : Loggable {
    private val logger = Logger.getGlobal()

    override fun log(message: String?) {
        if (level == Level.OFF) return
        logger.log(level, message)
    }
}
