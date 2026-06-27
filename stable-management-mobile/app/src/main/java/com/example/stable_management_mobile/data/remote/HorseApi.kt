package com.example.stable_management_mobile.data.remote

import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import retrofit2.http.GET

interface HorseApi {

    @GET("api/horses/{horseId}")
    suspend fun getHorseById(horseId: Int): HorseResponseDTO
}