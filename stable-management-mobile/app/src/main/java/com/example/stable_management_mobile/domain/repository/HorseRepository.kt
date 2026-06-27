package com.example.stable_management_mobile.domain.repository

import com.example.stable_management_mobile.data.remote.dto.HorseRequestDTO
import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.data.remote.dto.RatingRequestDTO
import com.example.stable_management_mobile.data.remote.dto.RatingResponseDTO

interface HorseRepository {
    suspend fun getHorseById(horseId: Int): Result<HorseResponseDTO>

    suspend fun getHorseRatings(horseId: Int): Result<List<RatingResponseDTO>>

    suspend fun addHorse(horse: HorseRequestDTO): Result<HorseResponseDTO>

    suspend fun addRating(horseRating: RatingRequestDTO,horseId: Int): Result<RatingResponseDTO>

    suspend fun updateHorse(horseId: Int, horseRequestDTO: HorseRequestDTO): Result<HorseResponseDTO>

    suspend fun updateHorseRating(horseId: Int, ratingId: Int,
                                  ratingRequestDTO: RatingRequestDTO): Result<RatingResponseDTO>

    suspend fun deleteHorse(horseId: Int): Result<Unit>

    suspend fun deleteRating(horseId: Int, ratingId: Int): Result<Unit>
}