package com.example.stable_management_mobile.data.remote

import com.example.stable_management_mobile.data.remote.dto.RatingResponseDTO
import retrofit2.http.GET

interface HorseApi {

    @GET("apis/horses/{horseId}")
    suspend fun getHorseRatings(horseId: Int): List<RatingResponseDTO>
}