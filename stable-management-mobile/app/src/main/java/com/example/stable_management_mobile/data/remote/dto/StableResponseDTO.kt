package com.example.stable_management_mobile.data.remote.dto

data class StableResponseDTO(
    val id: Int,
    val name: String,
    val capacity: Int,
    val horseCount: Int,
    val fillPercentage: Double
)