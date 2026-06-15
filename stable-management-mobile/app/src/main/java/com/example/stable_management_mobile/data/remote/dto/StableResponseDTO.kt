package com.example.stable_management_mobile.data.remote.dto

data class StableResponseDTO(
    val id: Int,
    val name: String,
    val maxCapacity: Int,
    val horseCount: Int,
    val fillPercentage: Double
)