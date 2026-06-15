package com.example.stable_management_mobile.ui.screens.stables

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stable_management_mobile.data.remote.dto.StableResponseDTO
import com.example.stable_management_mobile.domain.repository.StableRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class StablesUiState(
    val isLoading: Boolean = true,
    val stables: List<StableResponseDTO> = emptyList(),
    val errorMessage: String? = null
)

class StablesViewModel(
    private val repository: StableRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<StablesUiState>(StablesUiState())
    val uiState: StateFlow<StablesUiState> = _uiState

    init {
        fetchStables()
    }

    private fun fetchStables() {
        viewModelScope.launch {
            _uiState.value = StablesUiState(isLoading = true)
            repository.getStables()
                .onSuccess { stablesList -> _uiState.value =
                    StablesUiState(isLoading = false, stables = stablesList)}
                .onFailure { _ -> _uiState.value =
                StablesUiState(isLoading = false, errorMessage = "Failed to fetch stables")}
        }
    }
}