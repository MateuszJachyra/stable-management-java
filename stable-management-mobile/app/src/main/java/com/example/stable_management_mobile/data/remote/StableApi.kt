package com.example.stable_management_mobile.data.remote

import com.example.stable_management_mobile.data.remote.dto.StableResponseDTO
import retrofit2.http.GET

interface StableApi {
    @GET("/api/stables")
    suspend fun getAllStables(): List<StableResponseDTO>
}