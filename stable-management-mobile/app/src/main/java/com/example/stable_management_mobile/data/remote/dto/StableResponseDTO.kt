package com.example.stable_management_mobile.data.remote.dto

data class StableResponseDTO(
    val id: Int,
    val name: String,
    val horseCount: Int,
    val capacity: Int,
    val fillPercentage: Double
)