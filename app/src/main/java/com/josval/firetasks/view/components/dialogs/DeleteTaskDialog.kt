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
import com.josval.firetasks.viewmodel.AuthViewModel
import com.josval.firetasks.viewmodel.FirestoreViewModel

@Composable
fun DeleteTaskDialog(
    openState: MutableState<Boolean>,
    firestoreViewModel: FirestoreViewModel,
    taskId: MutableState<String>
) {
    if (openState.value) {
        AlertDialog(
            onDismissRequest = {
                openState.value = false
            },
            title = {
                Text(text = "Completar Tarea")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        firestoreViewModel.deleteTask(taskId.value)
                        openState.value = false
                    }
                ) {
                    Text("Eliminar Tarea")
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