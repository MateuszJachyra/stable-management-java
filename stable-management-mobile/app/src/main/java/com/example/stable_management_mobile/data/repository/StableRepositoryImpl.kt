package com.example.stable_management_mobile.data.repository

import com.example.stable_management_mobile.data.remote.StableApi
import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.data.remote.dto.StableResponseDTO
import com.example.stable_management_mobile.domain.repository.StableRepository

class StableRepositoryImpl(
    private val api: StableApi
) : StableRepository {
    override suspend fun getStables(): Result<List<StableResponseDTO>> {
        return try {
            val stables = api.getAllStables()
            Result.success(stables)
        }
        catch(e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHorsesByStable(stableName: String): Result<List<HorseResponseDTO>> {
        return try {
            val horses = api.getHorsesByStable(stableName)
            Result.success(horses)
        }
        catch(e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getStableByName(name: String): Result<StableResponseDTO> {
        return try{
            val stable = api.getStableByName(name)
            Result.success(stable)
        }
        catch(e: Exception) {
            Result.failure(e)
        }
    }

}