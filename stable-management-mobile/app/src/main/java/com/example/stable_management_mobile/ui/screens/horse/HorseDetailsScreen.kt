package com.example.stable_management_mobile.ui.screens.horse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.data.remote.dto.RatingResponseDTO
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorseDetailsScreen(
    viewModel: HorseDetailsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.shouldNavigateBack) {
        if (state.shouldNavigateBack) {
            onNavigateBack()
            viewModel.onNavigationHandled()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.horse?.name ?: "Loading...") },
                actions = {
                    IconButton(onClick = { viewModel.onEditHorseClicked() }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Horse")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onAddRatingClicked() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Rating")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.errorMessage != null) {
                Text(
                    text = "Error: ${state.errorMessage}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        HorseDetailHeader(state.horse)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Ratings", style = MaterialTheme.typography.titleMedium)
                    }
                    items(state.ratings) { rating ->
                        RatingCard(
                            rating = rating,
                            onEditClick = { viewModel.onEditRatingClicked(rating) }
                        )
                    }
                }
            }
        }
    }

    if (state.isHorseFormOpen) {
        ModalBottomSheet(onDismissRequest = { viewModel.onDismissHorseForm() }) {

            HorseForm(
                formTitle = "Edit Horse",
                horseName = state.horseName,
                horseBreed = state.horseBreed,
                horseType = state.horseType,
                horseStatus = state.horseStatus,
                horseAge = state.horseAge,
                horsePrice = state.horsePrice,
                horseWeight = state.horseWeight,
                errorMessage = state.horseFormErrorMessage,
                onNameChange = viewModel::onHorseNameChange,
                onBreedChange = viewModel::onHorseBreedChange,
                onTypeChange = viewModel::onHorseTypeChange,
                onStatusChange = viewModel::onHorseStatusChange,
                onAgeChange = viewModel::onHorseAgeChange,
                onPriceChange = viewModel::onHorsePriceChange,
                onWeightChange = viewModel::onHorseWeightChange,
                onSave = { viewModel.onSaveHorseChanges() },
                onDelete = { viewModel.onDeleteHorseClicked() }
            )
        }
    }

    if (state.isRatingFormOpen) {
        ModalBottomSheet(onDismissRequest = { viewModel.onDismissRatingForm() }) {
            RatingForm(
                state = state,
                onValueChange = viewModel::onRatingValueChange,
                onCommentChange = viewModel::onRatingCommentChange,
                onSave = { viewModel.onSaveRatingChanges() },
                onDelete = if (state.ratingSelected != null) ({ viewModel.onDeleteRatingClicked() }) else null
            )
        }
    }
}

@Composable
private fun HorseDetailHeader(horse: HorseResponseDTO?) {
    horse?.let {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Breed: ${it.breed}", style = MaterialTheme.typography.bodyLarge)
                Text("Type: ${it.type}", style = MaterialTheme.typography.bodyLarge)
                Text("Status: ${it.status}", style = MaterialTheme.typography.bodyLarge)
                Text("Age: ${it.age}", style = MaterialTheme.typography.bodyLarge)
                Text("Price: $${it.price}", style = MaterialTheme.typography.bodyLarge)
                Text("Weight: ${it.weight}kg", style = MaterialTheme.typography.bodyLarge)
                Text("Avg. Rating: ${"%.1f".format(it.averageRating)}/5.0",
                    style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun RatingCard(rating: RatingResponseDTO, onEditClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Rating: ${rating.value}/5", style = MaterialTheme.typography.titleMedium)
                if (!rating.comment.isNullOrBlank()) {
                    Text(rating.comment, style = MaterialTheme.typography.bodyMedium)
                }
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Rating")
            }
        }
    }
}