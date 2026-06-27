package com.example.stable_management_mobile.data.repository

import com.example.stable_management_mobile.data.remote.HorseApi
import com.example.stable_management_mobile.data.remote.dto.HorseRequestDTO
import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.data.remote.dto.RatingRequestDTO
import com.example.stable_management_mobile.data.remote.dto.RatingResponseDTO
import com.example.stable_management_mobile.domain.repository.HorseRepository

class HorseRepositoryImpl(
    private val api: HorseApi
): HorseRepository {
    override suspend fun getHorseById(horseId: Int): Result<HorseResponseDTO> {
        return safeApiCall { api.getHorseById(horseId) }
    }

    override suspend fun getHorseRatings(horseId: Int): Result<List<RatingResponseDTO>> {
        return safeApiCall{ api.getHorseRatings(horseId) }
    }

    override suspend fun addHorse(horse: HorseRequestDTO): Result<HorseResponseDTO> {
        return safeApiCall{ api.addHorse(horse) }
    }

    override suspend fun addRating(horseRating: RatingRequestDTO,horseId: Int): Result<RatingResponseDTO> {
        return safeApiCall{ api.addRating(horseRating, horseId) }
    }

    override suspend fun updateHorse(horseId: Int, horseRequestDTO: HorseRequestDTO): Result<HorseResponseDTO> {
        return safeApiCall{ api.updateHorse(horseId, horseRequestDTO) }
    }

    override suspend fun updateHorseRating(horseId: Int, ratingId: Int, ratingRequestDTO: RatingRequestDTO
    ): Result<RatingResponseDTO> {
        return safeApiCall { api.updateHorseRating(horseId, ratingId, ratingRequestDTO) }
    }

    override suspend fun deleteHorse(horseId: Int): Result<Unit> {
        return safeApiCall { api.deleteHorse(horseId) }
    }

    override suspend fun deleteRating(horseId: Int,ratingId: Int): Result<Unit> {
        return safeApiCall { api.deleteRating(horseId, ratingId) }
    }
}