package com.example.stable_management_mobile.data.remote.dto

data class RatingResponseDTO(
    val id: Int,
    val horseId: Int,
    val value: Int,
    val comment: String?
)