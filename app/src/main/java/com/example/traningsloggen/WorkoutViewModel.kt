package com.example.traningsloggen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class WorkoutUiState(
    val items: List<WorkoutItem> = emptyList(),
    val inputTitle: String = "",
    val inputCategory: String = "Bröst",
    val selectedCategoryFilter: String? = null,
    val editingItem: WorkoutItem? = null
)

class WorkoutViewModel : ViewModel() {
    var uiState by mutableStateOf(WorkoutUiState())
        private set

    fun updateInputTitle(title: String) {
        uiState = uiState.copy(inputTitle = title)
    }

    fun updateInputCategory(category: String) {
        uiState = uiState.copy(inputCategory = category)
    }

    fun updateCategoryFilter(category: String?) {
        uiState = uiState.copy(selectedCategoryFilter = category)
    }

    fun saveWorkout() {
        if (uiState.inputTitle.isBlank()) return

        if (uiState.editingItem == null) {
            val newItem = WorkoutItem(
                title = uiState.inputTitle.trim(),
                category = uiState.inputCategory,
                isCompleted = false
            )
            uiState = uiState.copy(
                items = uiState.items + newItem,
                inputTitle = ""
            )
        } else {
            val editing = uiState.editingItem ?: return
            val updatedList = uiState.items.map { item ->
                if (item.id == editing.id) {
                    item.copy(title = uiState.inputTitle.trim(), category = uiState.inputCategory)
                } else {
                    item
                }
            }
            uiState = uiState.copy(
                items = updatedList,
                inputTitle = "",
                editingItem = null
            )
        }
    }

    fun toggleCompletion(id: String) {
        val updatedList = uiState.items.map { item ->
            if (item.id == id) {
                item.copy(isCompleted = !item.isCompleted)
            } else {
                item
            }
        }
        uiState = uiState.copy(items = updatedList)
    }

    fun startEdit(item: WorkoutItem) {
        uiState = uiState.copy(
            inputTitle = item.title,
            inputCategory = item.category,
            editingItem = item
        )
    }

    fun deleteItem(id: String) {
        val updatedList = uiState.items.filter { it.id != id }
        val editing = if (uiState.editingItem?.id == id) null else uiState.editingItem
        val title = if (uiState.editingItem?.id == id) "" else uiState.inputTitle

        uiState = uiState.copy(
            items = updatedList,
            editingItem = editing,
            inputTitle = title
        )
    }
}