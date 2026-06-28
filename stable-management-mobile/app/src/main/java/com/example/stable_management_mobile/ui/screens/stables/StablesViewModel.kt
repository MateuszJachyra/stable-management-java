package com.example.stable_management_mobile.ui.screens.stables

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stable_management_mobile.data.remote.dto.StableRequestDTO
import com.example.stable_management_mobile.data.remote.dto.StableResponseDTO
import com.example.stable_management_mobile.domain.repository.StableRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StablesUiState(
    val isLoading: Boolean = true,
    val stables: List<StableResponseDTO> = emptyList(),
    val errorMessage: String? = null,

    val isFormOpen: Boolean = false,
    val formTitle: String = "",
    val stableSelected: StableResponseDTO? = null,

    val stableName: String = "",
    val capacity: String = "",
    val formErrorMessage: String? = null,
    val showForceDeleteDialog: Boolean = false
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
            _uiState.update { it.copy(isLoading = true) }
            repository.getStables()
                .onSuccess { stables ->
                    _uiState.update {
                        it.copy(isLoading = false, stables = stables, errorMessage = null)
                    }
                }
                .onFailure { _ ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Failed to fetch stables")
                    }
                }
        }
    }

    fun onEditStableClicked(stable: StableResponseDTO) {
        _uiState.update{
            it.copy(
                isFormOpen = true,
                formTitle = "Edit stable",
                stableSelected = stable,
                stableName = stable.name,
                capacity = stable.capacity.toString(),
                formErrorMessage = null
            )
        }
    }

    fun onAddStableClicked() {
        _uiState.update{
            it.copy(
                isFormOpen = true,
                formTitle = "Add stable",
                stableSelected = null,
                stableName = "",
                capacity = "",
                formErrorMessage = null
            )
        }
    }

    fun onStableNameChanged(name: String) {
        _uiState.update { it.copy(stableName = name) }
    }

    fun onMaxCapacityChanged(capacity: String) {
        _uiState.update { it.copy(capacity = capacity)}
    }

    fun onDismissForm() {
        _uiState.update { it.copy(isFormOpen = false) }
    }

    fun onSaveChanges() {
        viewModelScope.launch {
            _uiState.update { it.copy(formErrorMessage = null) }

            val state = _uiState.value
            val capacityInt = state.capacity.toIntOrNull()

            if (state.stableName.isBlank()) {
                _uiState.update { it.copy(formErrorMessage = "Name cannot be empty.") }
                return@launch
            }

            if (capacityInt == null || capacityInt <= 0) {
                _uiState.update { it.copy(formErrorMessage = "Capacity can't be zero or lower") }
                return@launch
            }

            val request = StableRequestDTO(
                name = state.stableName,
                capacity = capacityInt
            )

            val result = if (state.stableSelected == null) {
                repository.addStable(request)
            } else {
                repository.updateStable(state.stableSelected.name, request)
            }

            result.onSuccess {
                onDismissForm()
                fetchStables()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(formErrorMessage = error.message ?: "Something went wrong")
                }
            }
        }
    }

    fun onDeleteStableClicked() {
        val stableToDelete = _uiState.value.stableSelected ?: return
        viewModelScope.launch {
            repository.deleteStable(stableToDelete.name, false)
                .onSuccess {
                    onDismissForm()
                    fetchStables()
                }
                .onFailure { error ->
                    if (error.message?.contains("Stable not empty", ignoreCase = true) == true) {
                        _uiState.update { it.copy(showForceDeleteDialog = true) }
                    }
                    else {
                        _uiState.update {
                            it.copy(formErrorMessage = error.message ?: "Failed to delete stable")
                        }
                    }
                }
        }
    }

    fun onConfirmForceDelete() {
        val stableToDelete = _uiState.value.stableSelected ?: return
        viewModelScope.launch {
            repository.deleteStable(stableToDelete.name, true)
                .onSuccess {
                    _uiState.update { it.copy(showForceDeleteDialog = false) }
                    onDismissForm()
                    fetchStables()
                }
                .onFailure { error ->
                    _uiState.update{
                        it.copy(
                            showForceDeleteDialog = false,
                            formErrorMessage = error.message ?: "Failed to force delete stable"
                        )
                    }
                }
        }
    }

    fun onDismissForceDeleteDialog() {
        _uiState.update { it.copy(showForceDeleteDialog = false) }
    }
}