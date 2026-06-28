package com.example.stable_management_mobile.ui.screens.horse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.data.remote.dto.RatingResponseDTO
import com.example.stable_management_mobile.domain.repository.HorseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HorseDetailsUiState(
    val isLoading: Boolean = true,
    val horse: HorseResponseDTO? = null,
    val ratings: List<RatingResponseDTO> = emptyList(),
    val errorMessage: String? = null
)

class HorseDetailsViewModel(
    private val horseRepository: HorseRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val horseId: Int = checkNotNull(savedStateHandle["horseId"])
    private val _uiState = MutableStateFlow<HorseDetailsUiState>(HorseDetailsUiState())
    val uiState: StateFlow<HorseDetailsUiState> = _uiState

    init {
        fetchHorseDetails()
        fetchRatings()
    }

    private fun fetchHorseDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true)}
            horseRepository.getHorseById(horseId)
                .onSuccess{ horse ->
                    _uiState.update { it.copy(isLoading=false, horse = horse) }
                }
                .onFailure { _ ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to fetch horse details") }
                }
        }
    }

    private fun fetchRatings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true)}
            horseRepository.getHorseRatings(horseId)
                .onSuccess{ ratings ->
                    _uiState.update { it.copy(isLoading=false, ratings = ratings) }
                }
                .onFailure { _ ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to fetch horse ratings") }
                }
        }
    }

}