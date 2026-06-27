package com.example.stable_management_mobile.ui.screens.stables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@Composable
fun StableDetailsScreen(
    viewModel: StableDetailsViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        }
        else if (state.errorMessage != null) {
            Text(text = "Error: ${state.errorMessage}")
        }
        else {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .background(color = Color.LightGray)
                    .padding(top=32.dp)
                    .padding(16.dp)

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text=state.stableDetails!!.name.uppercase(), fontSize = 20.sp)
                    Text(text="Edit button WIP")
                }
                Text(text="${state.stableDetails!!.horseCount}/${state.stableDetails!!.maxCapacity}")
                Text(text="${state.stableDetails!!.fillPercentage}% filled")
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp),
            ) {
                items(items=state.horses) { horse ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(text = horse.name)
                            Text(text = "${horse.averageRating}")
                        }
                    }
                }
            }
        }
    }
}