package com.example.stable_management_mobile.data.remote

import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.data.remote.dto.StableResponseDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface StableApi {
    @GET("/api/stables")
    suspend fun getAllStables(): List<StableResponseDTO>

    @GET("/api/stables/{stableName}")
    suspend fun getStableByName(@Path("stableName") stableName: String): StableResponseDTO

    @GET("/api/stables/{stableName}/horses")
    suspend fun getHorsesByStable(@Path("stableName") stableName: String): List<HorseResponseDTO>


}