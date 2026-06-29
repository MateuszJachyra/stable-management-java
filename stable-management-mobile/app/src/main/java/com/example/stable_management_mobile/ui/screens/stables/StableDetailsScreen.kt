package com.example.stable_management_mobile.ui.screens.stables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import com.example.stable_management_mobile.data.remote.dto.HorseResponseDTO
import com.example.stable_management_mobile.data.remote.dto.StableResponseDTO
import com.example.stable_management_mobile.ui.screens.horse.HorseForm
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StableDetailsScreen(
    viewModel: StableDetailsViewModel = koinViewModel(),
    onHorseClicked: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    LaunchedEffect(state.shouldNavigateBack) {
        if (state.shouldNavigateBack) {
            onNavigateBack()
            viewModel.onNavigationHandled()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.stableDetails?.name ?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onEditStableClicked() }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Stable")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onAddHorseClicked() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Horse")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            if(state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.errorMessage != null) {
                Text(
                    text = "Error: ${state.errorMessage}",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        StableDetailHeader(state.stableDetails)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Horses", style = MaterialTheme.typography.titleMedium)
                    }
                    items(state.horses) { horse ->
                        HorseCard(
                            horse = horse,
                            onCardClick = { onHorseClicked(horse.id) },
                            onEditClick = { viewModel.onEditHorseClicked(horse) }
                        )
                    }
                }
            }
        }
    }

    if (state.isStableFormOpen) {
        ModalBottomSheet(onDismissRequest = { viewModel.onDismissStableForm() }) {
            Column(modifier = Modifier.padding(16.dp).padding(bottom=32.dp)) {
                Text("Edit Stable", style = MaterialTheme.typography.headlineSmall)
                OutlinedTextField(
                    value = state.stableCapacity,
                    onValueChange = viewModel::onStableCapacityChanged,
                    label = { Text("Capacity") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (state.stableFormErrorMessage != null) {
                    Text(
                        text = state.stableFormErrorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                Row {
                    Button(onClick = { viewModel.onDeleteStableClicked() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error)) { Text("Delete") }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { viewModel.onSaveStableChanges() }) { Text("Save") }
                }
            }
        }
    }

    if (state.isHorseFormOpen) {
        ModalBottomSheet(onDismissRequest = { viewModel.onDismissHorseForm() }) {
            HorseForm(
                formTitle = state.horseFormTitle,
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
                onDelete = if (state.horseSelected != null) ({ viewModel.onDeleteHorseClicked() }) else null
            )
        }
    }

    if (state.showForceDeleteStableDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissForceDeleteStableDialog() },
            title = { Text("Warning") },
            text = { Text("This stable is not empty. " +
                    "Deleting it will also delete all horses inside. Are you sure?") },
            confirmButton = {
                TextButton(onClick = { viewModel.onConfirmForceDeleteStable() }) { Text("Yes, Delete All") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDismissForceDeleteStableDialog() }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun StableDetailHeader(stable: StableResponseDTO?) {
    stable?.let {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Capacity: ${it.horseCount} / ${it.capacity}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Fill: ${"%.1f".format(it.fillPercentage)}%",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun HorseCard(
    horse: HorseResponseDTO,
    onCardClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = horse.name, style = MaterialTheme.typography.titleMedium)
                Text(text = horse.breed, style = MaterialTheme.typography.bodyMedium)
                Text(text = "Avg. Rating: ${"%.1f".format(horse.averageRating)}",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Horse")
            }
        }
    }
}