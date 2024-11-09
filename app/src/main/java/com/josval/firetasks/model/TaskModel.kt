package com.josval.firetasks.model

data class TaskModel(
    val title: String? = null,
    val body: String? = null,
    val priority: Priority = Priority.NONE,
    val createdBy: String? = null,
)
