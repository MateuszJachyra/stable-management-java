package com.example.stable_management_mobile.data.repository

import com.example.stable_management_mobile.data.remote.HorseApi
import com.example.stable_management_mobile.data.remote.dto.RatingResponseDTO
import com.example.stable_management_mobile.domain.repository.HorseRepository

class HorseRepositoryImpl(
    private val api: HorseApi
): HorseRepository {
    override suspend fun getHorseRatings(horseId: Int): Result<List<RatingResponseDTO>> {
        return try {
            val ratings = api.getHorseRatings(horseId)
            Result.success(ratings)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}