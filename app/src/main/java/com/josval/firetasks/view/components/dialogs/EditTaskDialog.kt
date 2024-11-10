package com.josval.firetasks.view.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.josval.firetasks.model.Priority
import com.josval.firetasks.model.TaskModel
import com.josval.firetasks.view.components.PriorityDropdown
import com.josval.firetasks.viewmodel.FirestoreViewModel

@Composable
fun EditTaskDialog(
    openState: MutableState<Boolean>,
    firestoreViewModel: FirestoreViewModel,
    task: MutableState<TaskModel>,
    taskId: MutableState<String>,
) {
    var title by remember { mutableStateOf("") }
    val titleMaxLength = 40
    title = task.value.title
    var body by remember { mutableStateOf("") }
    val bodyMaxLength = 100
    body = task.value.body
    var selectedPriority by remember { mutableStateOf("Ninguno") }
    selectedPriority = when (task.value.priority.toString()) {
        "HIGH" -> "Alto"
        "MEDIUM" -> "Medio"
        "LOW" -> "Bajo"
        "NONE" -> "Ninguno"
        else -> ""
    }
    if (openState.value) {
        AlertDialog(
            onDismissRequest = {
                openState.value = false
            },
            title = {
                Text(text = "Editar Tarea")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            if (it.length <= titleMaxLength) title = it
                        },
                        label = { Text("Título de la tarea") },
                        singleLine = false,
                        supportingText = {
                            Row {
                                Text("${title.length}/$titleMaxLength")
                            }
                        },
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        minLines = 3,
                        value = body,
                        onValueChange = {
                            if (it.length <= bodyMaxLength) body = it
                        },
                        label = { Text("Descripción de la tarea") },
                        singleLine = false,
                        supportingText = {
                            Row {
                                Text("${body.length}/$bodyMaxLength")
                            }
                        },
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PriorityDropdown(
                        selectedPriority = selectedPriority,
                        onPrioritySelected = { selectedPriority = it }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        firestoreViewModel.updateTask(
                            TaskModel(
                                title = title,
                                body = body,
                                priority = Priority.fromString(
                                    when (selectedPriority) {
                                        "Alto" -> "HIGH"
                                        "Medio" -> "MEDIUM"
                                        "Bajo" -> "LOW"
                                        "Ninguno" -> "NONE"
                                        else -> ""
                                    }
                                ),
                                createdBy = ""
                            ), taskId
                        )
                        openState.value = false
                        title = ""
                        body = ""
                        selectedPriority = "Ninguno"
                    }
                ) {
                    Text("Actualizar Tarea")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openState.value = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}