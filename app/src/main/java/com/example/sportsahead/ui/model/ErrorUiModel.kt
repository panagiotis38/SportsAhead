package com.example.sportsahead.ui.model

data class ErrorUiModel(
    val show: Boolean = false,
    val title: String = "",
    val message: String = "",
    val onTryAgain: () -> Unit = {}
)