package com.example.stable_management_mobile.domain.repository

import com.example.stable_management_mobile.data.remote.dto.StableResponseDTO

interface StableRepository {
    suspend fun getStables(): Result<List<StableResponseDTO>>
}