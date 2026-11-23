package com.example.traningsloggen

import java.util.UUID

data class WorkoutItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val category: String,
    val isCompleted: Boolean = false
)