package com.example.stable_management_mobile.data.remote

import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.data.remote.dto.StableRequestDTO
import com.example.stable_management_mobile.data.remote.dto.StableResponseDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface StableApi {
    @GET("/api/stables")
    suspend fun getAllStables(): List<StableResponseDTO>

    @GET("/api/stables/{stableName}")
    suspend fun getStableByName(@Path("stableName") stableName: String): StableResponseDTO

    @GET("/api/stables/{stableName}/horses")
    suspend fun getHorsesByStable(@Path("stableName") stableName: String): List<HorseResponseDTO>

    @POST("/api/stables")
    suspend fun addStable(@Body stable: StableRequestDTO): StableResponseDTO

    @PUT("/api/stables/{stableName}")
    suspend fun updateStable(@Path("stableName") stableName: String,
                             @Body stableRequestDTO: StableRequestDTO): StableResponseDTO

    @DELETE("/api/stables/{stableName}")
    suspend fun deleteStable(@Path("stableName") stableName: String,
                             @Query("force") force: Boolean): Unit
}