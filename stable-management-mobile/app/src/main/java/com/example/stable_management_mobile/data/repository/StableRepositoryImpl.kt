package com.example.stable_management_mobile.data.repository

import com.example.stable_management_mobile.data.remote.StableApi
import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.data.remote.dto.StableRequestDTO
import com.example.stable_management_mobile.data.remote.dto.StableResponseDTO
import com.example.stable_management_mobile.domain.repository.StableRepository

class StableRepositoryImpl(
    private val api: StableApi
) : StableRepository {
    override suspend fun getStables(): Result<List<StableResponseDTO>> {
        return safeApiCall { api.getAllStables() }
    }

    override suspend fun getHorsesByStable(stableName: String): Result<List<HorseResponseDTO>> {
        return safeApiCall { api.getHorsesByStable(stableName) }
    }

    override suspend fun getStableByName(name: String): Result<StableResponseDTO> {
        return safeApiCall { api.getStableByName(name) }
    }

    override suspend fun addStable(stable: StableRequestDTO): Result<StableResponseDTO> {
        return safeApiCall { api.addStable(stable) }
    }

    override suspend fun updateStable(stableName: String, stable: StableRequestDTO): Result<StableResponseDTO> {
        return safeApiCall { api.updateStable(stableName, stable) }
    }

    override suspend fun deleteStable(stableName: String, force: Boolean): Result<Unit> {
        return safeApiCall { api.deleteStable(stableName,force) }
    }
}