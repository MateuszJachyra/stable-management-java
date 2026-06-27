package com.example.stable_management_mobile.ui.screens.stables

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.data.remote.dto.StableResponseDTO
import com.example.stable_management_mobile.domain.repository.StableRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StableDetailsUiState(
    val isLoading: Boolean = true,
    val stableDetails: StableResponseDTO? = null,
    val horses: List<HorseResponseDTO> = emptyList(),
    val errorMessage: String? = null
)

class StableDetailsViewModel(
    private val stableRepository: StableRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val stableName: String = checkNotNull(savedStateHandle["stableName"])
    private val _uiState = MutableStateFlow<StableDetailsUiState>(StableDetailsUiState())
    val uiState: StateFlow<StableDetailsUiState> = _uiState

    init{
        fetchStableDetails()
        fetchHorses()
    }

    private fun fetchStableDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            stableRepository.getStableByName(stableName)
                .onSuccess { stableDetails ->
                    _uiState.update {
                        it.copy(stableDetails = stableDetails)
                    }
                }
                .onFailure { _ ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Failed to fetch stable details")
                    }
                }
        }
    }

    private fun fetchHorses() {
        viewModelScope.launch {
            stableRepository.getHorsesByStable(stableName)
                .onSuccess { horses -> _uiState.update {
                        it.copy(isLoading = false, horses = horses)
                    }
                }
                .onFailure { _ -> _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Failed to fetch horses")
                    }
                }
        }
    }
}