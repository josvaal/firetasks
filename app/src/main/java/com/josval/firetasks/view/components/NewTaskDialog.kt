package com.josval.firetasks.view.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun NewTaskDialog(
    openState: MutableState<Boolean>
) {
    if (openState.value) {
        AlertDialog(
            onDismissRequest = {
                openState.value = false
            },
            title = {
                Text(text = "Documentación  de AlertDialog")
            },
            text = {
                Text(
                    "Descripción de la alerta de ejemplo de material 2."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openState.value = false
                    }
                ) {
                    Text("Crear Tarea")
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