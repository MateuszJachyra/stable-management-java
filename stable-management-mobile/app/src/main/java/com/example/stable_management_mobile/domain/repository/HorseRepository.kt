package com.example.stable_management_mobile.domain.repository

import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO

interface HorseRepository {
    suspend fun getHorseById(horseId: Int): Result<HorseResponseDTO>
}