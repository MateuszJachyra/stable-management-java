package com.example.stable_management_mobile.domain.repository

import com.example.stable_management_mobile.data.remote.dto.RatingResponseDTO

interface HorseRepository {
    suspend fun getHorseRatings(horseId: Int): Result<List<RatingResponseDTO>>
}