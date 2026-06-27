package com.example.stable_management_mobile.data.remote.dto

data class HorseRequestDTO (
    val name: String,
    val breed: String,
    val type: String,
    val status: String,
    val age: Int,
    val price: Double,
    val weight: Double,
    val stableName: String,
)