package com.example.android_engineer_technical_assignment.data
import com.google.gson.annotations.SerializedName

data class Movie (
    @SerializedName("title") val title: String?,
    @SerializedName("poster_path") val posterpath: String?,
    @SerializedName("overview") val overview: String?
)