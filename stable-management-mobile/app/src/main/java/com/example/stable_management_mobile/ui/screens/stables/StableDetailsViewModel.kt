package com.example.stable_management_mobile.ui.screens.stables

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stable_management_mobile.data.remote.dto.HorseRequestDTO
import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.data.remote.dto.StableRequestDTO
import com.example.stable_management_mobile.data.remote.dto.StableResponseDTO
import com.example.stable_management_mobile.domain.model.HorseStatus
import com.example.stable_management_mobile.domain.model.HorseType
import com.example.stable_management_mobile.domain.repository.HorseRepository
import com.example.stable_management_mobile.domain.repository.StableRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StableDetailsUiState(
    val isLoading: Boolean = true,
    val stableDetails: StableResponseDTO? = null,
    val horses: List<HorseResponseDTO> = emptyList(),
    val errorMessage: String? = null,

    val isStableFormOpen: Boolean = false,
    val stableCapacity: String = "",
    val stableFormErrorMessage: String? = null,
    val showForceDeleteStableDialog: Boolean = false,
    val shouldNavigateBack: Boolean = false,

    val isHorseFormOpen: Boolean = false,
    val horseFormTitle: String = "",
    val horseSelected: HorseResponseDTO? = null,
    val horseName: String = "",
    val horseBreed: String = "",
    val horseType: HorseType? = null,
    val horseStatus: HorseStatus? = null,
    val horseAge: String = "",
    val horsePrice: String = "",
    val horseWeight: String = "",
    val horseFormErrorMessage: String? = null,
)

class StableDetailsViewModel(
    private val stableRepository: StableRepository,
    private val horseRepository: HorseRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val stableName: String = checkNotNull(savedStateHandle["stableName"])
    private val _uiState = MutableStateFlow<StableDetailsUiState>(StableDetailsUiState())
    val uiState: StateFlow<StableDetailsUiState> = _uiState

    fun refresh() {
        viewModelScope.launch {
            val showLoading = _uiState.value.stableDetails == null && _uiState.value.horses.isEmpty()
            if (showLoading) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            }
            val stableResult = stableRepository.getStableByName(stableName)
            val horsesResult = stableRepository.getHorsesByStable(stableName)

            when {
                stableResult.isFailure -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Failed to fetch stable details")
                }
                horsesResult.isFailure -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Failed to fetch horses")
                }
                else -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        stableDetails = stableResult.getOrNull(),
                        horses = horsesResult.getOrNull() ?: emptyList(),
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun onEditStableClicked() {
        val stable = _uiState.value.stableDetails ?: return
        _uiState.update {
            it.copy(
                isStableFormOpen = true,
                stableCapacity = stable.capacity.toString(),
                stableFormErrorMessage = null
            )
        }
    }

    fun onDismissStableForm() {
        _uiState.update { it.copy(isStableFormOpen = false) }
    }

    fun onStableCapacityChanged(capacity: String) {
        _uiState.update { it.copy(stableCapacity = capacity) }
    }

    fun onSaveStableChanges() = viewModelScope.launch {
        val state = _uiState.value
        val stableToUpdate = state.stableDetails ?: return@launch
        val capacityInt = state.stableCapacity.toIntOrNull()

        if (capacityInt == null || capacityInt <= 0) {
            _uiState.update { it.copy(stableFormErrorMessage = "Capacity must be a positive number.") }
            return@launch
        }

        val request = StableRequestDTO(name = stableToUpdate.name, capacity = capacityInt)
        stableRepository.updateStable(stableToUpdate.name, request).onSuccess {
            onDismissStableForm()
            refresh()
        }.onFailure { error ->
            _uiState.update { it.copy(stableFormErrorMessage = error.message) }
        }
    }

    fun onDeleteStableClicked() = viewModelScope.launch {
        val stableToDelete = _uiState.value.stableDetails ?: return@launch
        stableRepository.deleteStable(stableToDelete.name, force = false).onSuccess {
            _uiState.update { it.copy(shouldNavigateBack = true) }
        }.onFailure { error ->
            if (error.message?.contains("not empty", ignoreCase = true) == true) {
                _uiState.update { it.copy(showForceDeleteStableDialog = true) }
            } else {
                _uiState.update { it.copy(stableFormErrorMessage = error.message) }
            }
        }
    }

    fun onConfirmForceDeleteStable() = viewModelScope.launch {
        val stableToDelete = _uiState.value.stableDetails ?: return@launch
        stableRepository.deleteStable(stableToDelete.name, force = true).onSuccess {
            _uiState.update { it.copy(showForceDeleteStableDialog = false) }
            _uiState.update { it.copy(shouldNavigateBack = true) }
        }.onFailure { error ->
            _uiState.update { it.copy(showForceDeleteStableDialog = false, stableFormErrorMessage = error.message) }
        }
    }

    fun onDismissForceDeleteStableDialog() {
        _uiState.update { it.copy(showForceDeleteStableDialog = false) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(shouldNavigateBack = false) }
    }

    fun onAddHorseClicked() {
        _uiState.update {
            it.copy(
                isHorseFormOpen = true,
                horseFormTitle = "Add Horse",
                horseSelected = null,
                horseName = "",
                horseBreed = "",
                horseType = null,
                horseStatus = null,
                horseAge = "",
                horsePrice = "",
                horseWeight = "",
                horseFormErrorMessage = null
            )
        }
    }

    fun onEditHorseClicked(horse: HorseResponseDTO) {
        _uiState.update {
            it.copy(
                isHorseFormOpen = true,
                horseFormTitle = "Edit Horse",
                horseSelected = horse,
                horseName = horse.name,
                horseBreed = horse.breed,
                horseType = runCatching { HorseType.valueOf(horse.type.uppercase()) }.getOrNull(),
                horseStatus = runCatching { HorseStatus.valueOf(horse.status.uppercase()) }.getOrNull(),
                horseAge = horse.age.toString(),
                horsePrice = horse.price.toString(),
                horseWeight = horse.weight.toString(),
                horseFormErrorMessage = null
            )
        }
    }

    fun onDismissHorseForm() {
        _uiState.update { it.copy(isHorseFormOpen = false) }
    }

    fun onHorseNameChange(name: String) { _uiState.update { it.copy(horseName = name) } }
    fun onHorseBreedChange(breed: String) { _uiState.update { it.copy(horseBreed = breed) } }
    fun onHorseTypeChange(type: HorseType) { _uiState.update { it.copy(horseType = type) } }
    fun onHorseStatusChange(status: HorseStatus) { _uiState.update { it.copy(horseStatus = status) } }
    fun onHorseAgeChange(age: String) { _uiState.update { it.copy(horseAge = age) } }
    fun onHorsePriceChange(price: String) { _uiState.update { it.copy(horsePrice = price) } }
    fun onHorseWeightChange(weight: String) { _uiState.update { it.copy(horseWeight = weight) } }

    fun onSaveHorseChanges() = viewModelScope.launch {
        val state = _uiState.value
        val ageInt = state.horseAge.toIntOrNull()
        val priceDouble = state.horsePrice.toDoubleOrNull()
        val weightDouble = state.horseWeight.toDoubleOrNull()

        if (state.horseName.isBlank()) {
            _uiState.update { it.copy(horseFormErrorMessage = "Name cannot be empty.") }; return@launch
        }
        if (state.horseType == null || state.horseStatus == null) {
            _uiState.update { it.copy(horseFormErrorMessage = "Type and Status must be selected.") }; return@launch
        }
        if (ageInt == null || ageInt < 0) {
            _uiState.update { it.copy(horseFormErrorMessage = "Age must be a non-negative number.") }; return@launch
        }
        if (priceDouble == null || priceDouble < 0.0) {
            _uiState.update { it.copy(horseFormErrorMessage = "Price must be a non-negative number.") }; return@launch
        }
        if (weightDouble == null || weightDouble <= 0.0) {
            _uiState.update { it.copy(horseFormErrorMessage = "Weight must be a positive number.") }; return@launch
        }
        _uiState.update { it.copy(horseFormErrorMessage = null) }

        val request = HorseRequestDTO(
            name = state.horseName,
            breed = state.horseBreed,
            type = state.horseType.name,
            status = state.horseStatus.name,
            age = ageInt,
            price = priceDouble,
            weight = weightDouble,
            stableName = state.stableDetails?.name ?: ""
        )

        val result = if (state.horseSelected == null) {
            horseRepository.addHorse(request)
        } else {
            horseRepository.updateHorse(state.horseSelected.id, request)
        }

        result.onSuccess {
            onDismissHorseForm()
            refresh()
        }.onFailure { error ->
            _uiState.update { it.copy(horseFormErrorMessage = error.message) }
        }
    }

    fun onDeleteHorseClicked() = viewModelScope.launch {
        val horseToDelete = _uiState.value.horseSelected ?: return@launch
        horseRepository.deleteHorse(horseToDelete.id).onSuccess {
            onDismissHorseForm()
            refresh()
        }.onFailure { error ->
            _uiState.update { it.copy(horseFormErrorMessage = error.message) }
        }
    }
}