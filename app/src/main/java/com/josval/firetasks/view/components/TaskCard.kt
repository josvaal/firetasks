package com.josval.firetasks.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.josval.firetasks.R
import com.josval.firetasks.model.Priority
import com.josval.firetasks.model.TaskModel
import com.josval.firetasks.viewmodel.FirestoreViewModel

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    task: TaskModel,
    id: String,
    firestoreViewModel: FirestoreViewModel,
    editState: MutableState<Boolean>,
    mutableTask: MutableState<TaskModel>,
    mutableTaskId: MutableState<String>,
    deleteState: MutableState<Boolean>
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(2.dp, task.priority.color),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(10.dp),
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_icon_book),
                    contentDescription = "Ícono de tarea",
                    tint = task.priority.color
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = task.body,
                style = MaterialTheme.typography.bodyMedium
            )


            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    deleteState.value = true
                    mutableTaskId.value = id
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon_done),
                        contentDescription = "Marcar como Terminado",
                        tint = task.priority.color
                    )
                }
                IconButton(onClick = {
                    editState.value = true
                    mutableTask.value = TaskModel(
                        title = task.title,
                        body = task.body,
                        priority = Priority.fromString(task.priority.toString()),
                        createdBy = task.createdBy
                    )
                    mutableTaskId.value = id
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon_edit),
                        contentDescription = "Editar Tarea",
                        tint = task.priority.color
                    )
                }
            }
        }
    }
}