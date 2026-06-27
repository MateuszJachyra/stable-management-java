package com.example.stable_management_mobile.domain.repository

import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.data.remote.dto.StableRequestDTO
import com.example.stable_management_mobile.data.remote.dto.StableResponseDTO

interface StableRepository {
    suspend fun getStables(): Result<List<StableResponseDTO>>

    suspend fun getHorsesByStable(stableName: String): Result<List<HorseResponseDTO>>

    suspend fun getStableByName(name: String): Result<StableResponseDTO>

    suspend fun addStable(stable: StableRequestDTO): Result<StableResponseDTO>

    suspend fun updateStable(stable: StableRequestDTO, stableName: String): Result<StableResponseDTO>

    suspend fun deleteStable(stableName: String): Result<Unit>
}