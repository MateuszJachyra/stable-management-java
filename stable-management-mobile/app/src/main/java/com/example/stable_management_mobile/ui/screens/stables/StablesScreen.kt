package com.example.stable_management_mobile.ui.screens.stables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stable_management_mobile.data.remote.dto.StableResponseDTO
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StablesScreen(
    viewModel: StablesViewModel = koinViewModel(),
    onStableClicked: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onAddStableClicked() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Stable")
            }
        }
    ) {
        Box(
           modifier = Modifier.fillMaxSize().padding(it)
        ) {
            if(state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else if(state.errorMessage != null) {
                Text(
                    text = "Error: ${state.errorMessage}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.stables) { stable ->
                        StableCard(
                            stable = stable,
                            onCardClick = { onStableClicked(stable.name) },
                            onEditClick = { viewModel.onEditStableClicked(stable) }
                        )
                    }
                }
            }
        }
    }

    if (state.isFormOpen) {
        ModalBottomSheet(onDismissRequest = { viewModel.onDismissForm() }) {
            StableForm(
                state = state,
                onNameChange = viewModel::onStableNameChanged,
                onCapacityChange = viewModel::onMaxCapacityChanged,
                onSave = { viewModel.onSaveChanges() },
                onDelete = if (state.stableSelected != null) ({ viewModel.onDeleteStableClicked() }) else null
            )
        }
    }

    if (state.showForceDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissForceDeleteDialog() },
            title = {Text("Warning")},
            text = {Text("Stable contains horses. Are you sure to delete this stable?") },
            confirmButton = {
                TextButton(onClick = { viewModel.onConfirmForceDelete()}) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDismissForceDeleteDialog()}) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
fun StableCard(
    stable: StableResponseDTO,
    onCardClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stable.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Capacity: ${stable.horseCount} / ${stable.capacity}",
                )
                Text(
                    text = "Fill: ${stable.fillPercentage}%"
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Stable")
            }
        }
    }
}