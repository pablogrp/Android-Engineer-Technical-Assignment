package com.example.android_engineer_technical_assignment.data
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class Movie (
    @SerializedName("title") val title: String?,
    @SerializedName("poster_path") val poster_path: String?,
    @SerializedName("overview") val overview: String?
)