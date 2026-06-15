package com.example.stable_management_mobile.data.repository

import com.example.stable_management_mobile.data.remote.StableApi
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
}