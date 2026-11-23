package com.example.traningsloggen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.traningsloggen.ui.theme.TraningsloggenTheme

class MainActivity : ComponentActivity() {
    private val viewModel: WorkoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TraningsloggenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WorkoutScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(viewModel: WorkoutViewModel) {
    val state = viewModel.uiState

    val totalCount = state.items.size
    val completedCount = state.items.count { it.isCompleted }

    val filterOptions = listOf("Alla", "Bröst", "Ben", "Rygg", "Cardio")
    val filteredItems = if (state.selectedCategoryFilter == null) {
        state.items
    } else {
        state.items.filter { it.category == state.selectedCategoryFilter }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Träningsloggen") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Statistikrad
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Statistik: $completedCount av $totalCount övningar klara",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Textfält för ny eller redigerad övning
            OutlinedTextField(
                value = state.inputTitle,
                onValueChange = { viewModel.updateInputTitle(it) },
                label = { Text("Skriv in övning (t.ex. Bänkpress)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Välj kategori för övningen
            Text(
                text = "Kategori för övningen:",
                style = MaterialTheme.typography.labelLarge
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val categories = listOf("Bröst", "Ben", "Rygg", "Cardio")
                items(categories) { category ->
                    FilterChip(
                        selected = state.inputCategory == category,
                        onClick = { viewModel.updateInputCategory(category) },
                        label = { Text(category) }
                    )
                }
            }

            // Spara / Redigera-knapp
            Button(
                onClick = { viewModel.saveWorkout() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.editingItem == null) "Lägg till övning" else "Spara ändring")
            }

            HorizontalDivider()

            // Filterrad för listan
            Text(
                text = "Visa kategori:",
                style = MaterialTheme.typography.labelLarge
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { option ->
                    val isSelected = if (option == "Alla") {
                        state.selectedCategoryFilter == null
                    } else {
                        state.selectedCategoryFilter == option
                    }

                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            viewModel.updateCategoryFilter(if (option == "Alla") null else option)
                        },
                        label = { Text(option) }
                    )
                }
            }

            // Lista eller tomt läge
            if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    val emptyMessage = if (state.items.isEmpty()) {
                        "Inga övningar tillagda ännu.\nFyll i ovan och klicka på knappen för att komma igång!"
                    } else {
                        "Inga övningar i kategorin ${state.selectedCategoryFilter}."
                    }
                    Text(
                        text = emptyMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredItems, key = { it.id }) { item ->
                        WorkoutRow(
                            item = item,
                            onToggle = { viewModel.toggleCompletion(item.id) },
                            onEdit = { viewModel.startEdit(item) },
                            onDelete = { viewModel.deleteItem(item.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutRow(
    item: WorkoutItem,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        colors = CardDefaults.cardColors(
            containerColor = if (item.isCompleted)
                MaterialTheme.colorScheme.surfaceVariant
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Kategori: ${item.category} | ${if (item.isCompleted) "Klar ✓" else "Ej klar"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Redigera")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Ta bort")
                }
            }
        }
    }
}