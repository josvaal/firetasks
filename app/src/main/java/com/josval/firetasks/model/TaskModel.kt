package com.josval.firetasks.model

data class TaskModel(
    val title: String,
    val body: String,
    val priority: Priority = Priority.NONE,
    val createdBy: String,
)
