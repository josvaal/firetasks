package com.josval.firetasks.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.josval.firetasks.R
import com.josval.firetasks.model.PriorityView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityDropdown(
    selectedPriority: String,
    onPrioritySelected: (String) -> Unit
) {
    val priorities = listOf("Alto", "Medio", "Bajo", "Ninguno")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedPriority,
            onValueChange = {}, // No se usa, el valor se actualiza desde el menú
            readOnly = true,
            label = { Text("Prioridad") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            PriorityView.entries.forEach { priority ->
                DropdownMenuItem(
                    text = { Text(priority.text) },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_icon_dot),
                            contentDescription = "Color de Prioridad",
                            tint = priority.color
                        )
                    },
                    onClick = {
                        onPrioritySelected(priority.text)
                        expanded = false
                    }
                )
            }
        }
    }
}
