package com.example.stable_management_mobile.data.repository

import com.example.stable_management_mobile.data.remote.HorseApi
import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.domain.repository.HorseRepository

class HorseRepositoryImpl(
    private val api: HorseApi
): HorseRepository {
    override suspend fun getHorseById(horseId: Int): Result<HorseResponseDTO> {
        return try {
            val horse = api.getHorseById(horseId)
            Result.success(horse)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}