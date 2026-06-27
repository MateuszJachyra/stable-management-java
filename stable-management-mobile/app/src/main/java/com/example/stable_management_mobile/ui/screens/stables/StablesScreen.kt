package com.example.stable_management_mobile.ui.screens.stables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun StablesScreen(
    viewModel: StablesViewModel = koinViewModel(),
    onStableClicked: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Box(
       modifier = Modifier.fillMaxSize()
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
                    Card (
                        modifier = Modifier.fillMaxWidth()
                            .clickable(onClick = { onStableClicked(stable.name) })
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)

                        ) {
                            Text(
                                text = stable.name,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "Capacity: ${stable.horseCount} / ${stable.maxCapacity}",
                            )
                            Text(
                                text = "Fill: ${stable.fillPercentage}%"
                            )
                        }
                    }
                }
            }
        }
    }
}