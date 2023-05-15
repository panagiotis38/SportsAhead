package com.example.data.model

import com.google.gson.annotations.SerializedName

data class FavouriteModel(
    @SerializedName("favourites")
    val favouritesMap: MutableMap<String, MutableList<String>> = mutableMapOf()
)