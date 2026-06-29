package com.example.stable_management_mobile.ui.screens.horse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stable_management_mobile.data.remote.dto.HorseRequestDTO
import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.data.remote.dto.RatingRequestDTO
import com.example.stable_management_mobile.data.remote.dto.RatingResponseDTO
import com.example.stable_management_mobile.domain.model.HorseStatus
import com.example.stable_management_mobile.domain.model.HorseType
import com.example.stable_management_mobile.domain.repository.HorseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HorseDetailsUiState(
    val isLoading: Boolean = true,
    val horse: HorseResponseDTO? = null,
    val ratings: List<RatingResponseDTO> = emptyList(),
    val errorMessage: String? = null,

    val isHorseFormOpen: Boolean = false,
    val horseName: String = "",
    val horseBreed: String = "",
    val horseType: HorseType? = null,
    val horseStatus: HorseStatus? = null,
    val horseAge: String = "",
    val horsePrice: String = "",
    val horseWeight: String = "",
    val horseFormErrorMessage: String? = null,
    val shouldNavigateBack: Boolean = false,

    val isRatingFormOpen: Boolean = false,
    val ratingFormTitle: String = "",
    val ratingSelected: RatingResponseDTO? = null,
    val ratingValue: String = "",
    val ratingComment: String? = null,
    val ratingFormErrorMessage: String? = null
)

class HorseDetailsViewModel(
    private val horseRepository: HorseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val horseId: Int = checkNotNull(savedStateHandle["horseId"])

    private val _uiState = MutableStateFlow(HorseDetailsUiState())
    val uiState: StateFlow<HorseDetailsUiState> = _uiState.asStateFlow()

    init {
        fetchHorseDetails()
        fetchRatings()
    }

    private fun fetchHorseDetails() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        horseRepository.getHorseById(horseId).onSuccess { horse ->
            _uiState.update { it.copy(isLoading = false, horse = horse) }
        }.onFailure { error ->
            _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
        }
    }

    private fun fetchRatings() = viewModelScope.launch {
        horseRepository.getHorseRatings(horseId).onSuccess { ratings ->
            _uiState.update { it.copy(ratings = ratings) }
        }.onFailure { error ->
            _uiState.update { it.copy(errorMessage = error.message) }
        }
    }

    fun onEditHorseClicked() {
        val horse = _uiState.value.horse ?: return
        _uiState.update {
            it.copy(
                isHorseFormOpen = true,
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
        val horseToUpdate = state.horse ?: return@launch

        val ageInt = state.horseAge.toIntOrNull()
        val priceDouble = state.horsePrice.toDoubleOrNull()
        val weightDouble = state.horseWeight.toDoubleOrNull()

        if (state.horseName.isBlank() || state.horseType == null || state.horseStatus == null || ageInt == null
            || priceDouble == null || weightDouble == null) {
            _uiState.update { it.copy(horseFormErrorMessage = "All fields must be filled correctly.") }
            return@launch
        }

        val request = HorseRequestDTO(
            name = state.horseName,
            breed = state.horseBreed,
            type = state.horseType.name,
            status = state.horseStatus.name,
            age = ageInt,
            price = priceDouble,
            weight = weightDouble,
            stableName = horseToUpdate.stableName
        )

        horseRepository.updateHorse(horseToUpdate.id, request).onSuccess {
            onDismissHorseForm()
            fetchHorseDetails()
        }.onFailure { error ->
            _uiState.update { it.copy(horseFormErrorMessage = error.message) }
        }
    }

    fun onAddRatingClicked() {
        _uiState.update {
            it.copy(
                isRatingFormOpen = true,
                ratingFormTitle = "Add Rating",
                ratingSelected = null,
                ratingValue = "",
                ratingComment = "",
                ratingFormErrorMessage = null
            )
        }
    }

    fun onEditRatingClicked(rating: RatingResponseDTO) {
        _uiState.update {
            it.copy(
                isRatingFormOpen = true,
                ratingFormTitle = "Edit Rating",
                ratingSelected = rating,
                ratingValue = rating.value.toString(),
                ratingComment = rating.comment,
                ratingFormErrorMessage = null
            )
        }
    }

    fun onDismissRatingForm() {
        _uiState.update { it.copy(isRatingFormOpen = false) }
    }

    fun onRatingValueChange(value: String) {
        if (value.all { it.isDigit() }) {
            _uiState.update { it.copy(ratingValue = value) }
        }
    }

    fun onRatingCommentChange(comment: String) {
        _uiState.update { it.copy(ratingComment = comment) }
    }

    fun onSaveRatingChanges() = viewModelScope.launch {
        val state = _uiState.value
        val ratingValueInt = state.ratingValue.toIntOrNull()

        if (ratingValueInt == null || ratingValueInt !in 1..5) {
            _uiState.update { it.copy(ratingFormErrorMessage = "Rating must be a number between 1 and 5.") }
            return@launch
        }

        val request = RatingRequestDTO(value = ratingValueInt, comment = state.ratingComment)

        val result =
            if (state.ratingSelected == null) {
                horseRepository.addRating(horseId = horseId, horseRating = request)
            } else {
                horseRepository.updateHorseRating(horseId, state.ratingSelected.id, request)
            }


        result.onSuccess {
            onDismissRatingForm()
            fetchRatings()
            fetchHorseDetails()
        }.onFailure { error ->
            _uiState.update { it.copy(ratingFormErrorMessage = error.message) }
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(shouldNavigateBack = false) }
    }

    fun onDeleteHorseClicked() = viewModelScope.launch {
        horseRepository.deleteHorse(horseId).onSuccess {
            _uiState.update { it.copy(shouldNavigateBack = true) }
        }.onFailure { error ->
            _uiState.update { it.copy(horseFormErrorMessage = error.message) }
        }
    }

    fun onDeleteRatingClicked() = viewModelScope.launch {
        val ratingToDelete = _uiState.value.ratingSelected ?: return@launch
        horseRepository.deleteRating(horseId, ratingToDelete.id).onSuccess {
            onDismissRatingForm()
            fetchRatings()
            fetchHorseDetails()
        }.onFailure { error ->
            _uiState.update { it.copy(ratingFormErrorMessage = error.message) }
        }
    }
}