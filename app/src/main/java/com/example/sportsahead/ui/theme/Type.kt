package com.example.sportsahead.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.sportsahead.R

val sportsAheadFontFamily = FontFamily(
    Font(R.font.font_sports_ahead, FontWeight.Normal),
)
val Typography = Typography(
    defaultFontFamily = sportsAheadFontFamily
)