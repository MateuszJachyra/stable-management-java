package com.example.stable_management_mobile.domain.repository

import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.data.remote.dto.StableResponseDTO

interface StableRepository {
    suspend fun getStables(): Result<List<StableResponseDTO>>

    suspend fun getHorsesByStable(stableName: String): Result<List<HorseResponseDTO>>

    suspend fun getStableByName(name: String): Result<StableResponseDTO>
}