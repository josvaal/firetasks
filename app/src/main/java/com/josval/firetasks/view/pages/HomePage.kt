package com.josval.firetasks.view.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.josval.firetasks.R
import com.josval.firetasks.model.Priority
import com.josval.firetasks.model.TaskModel
import com.josval.firetasks.view.components.dialogs.EditTaskDialog
import com.josval.firetasks.view.components.dialogs.NewTaskDialog
import com.josval.firetasks.viewmodel.AuthState
import com.josval.firetasks.viewmodel.AuthViewModel
import com.josval.firetasks.viewmodel.FirestoreState
import com.josval.firetasks.viewmodel.FirestoreViewModel
import com.josval.firetasks.view.components.TaskCard
import com.josval.firetasks.view.components.dialogs.DeleteTaskDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    firestoreViewModel: FirestoreViewModel
) {
    val openNewTaskDialog = remember { mutableStateOf(false) }
    val openEditTaskDialog = remember { mutableStateOf(false) }
    val openDeleteTaskDialog = remember { mutableStateOf(false) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    val profile = authViewModel.profile()
    val uid = profile?.uid ?: return

    val firestoreState = firestoreViewModel.firestoreState.observeAsState()
    val tasks = firestoreViewModel.tasks.observeAsState(emptyList()).value

    val toastMessage = firestoreViewModel.toastMessage.observeAsState()

    val toEditTask = remember {
        mutableStateOf(
            TaskModel(
                title = "",
                body = "",
                priority = Priority.NONE,
                createdBy = uid
            )
        )
    }
    val currentTaskId = remember { mutableStateOf("") }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                navController.popBackStack()
                navController.navigate("login")
            }

            is AuthState.Authenticated -> {
                firestoreViewModel.tasksSnapshot(uid)
            }

            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    LaunchedEffect(toastMessage.value) {
        toastMessage.value?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            firestoreViewModel.clearToastMessage()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_icon_fire),
                            contentDescription = "Logo Fire Tasks",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.app_name),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("profile")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_icon_person),
                            contentDescription = "Cerrar Sesión",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openNewTaskDialog.value = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Tarea")
            }
        },
        content = { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    when (firestoreState.value) {
                        is FirestoreState.Loading -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier)
                            }
                        }

                        is FirestoreState.Error -> {
                            val errorMessage =
                                (firestoreState.value as FirestoreState.Error).message
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Error: $errorMessage",
                                    color = Color.Red,
                                    modifier = Modifier
                                )
                            }
                        }

                        is FirestoreState.Fetched -> {
                            Column(modifier = Modifier.fillMaxSize()) {
                                tasks.forEach { (id, task) ->
                                    TaskCard(
                                        task = task,
                                        id = id,
                                        firestoreViewModel = firestoreViewModel,
                                        editState = openEditTaskDialog,
                                        mutableTask = toEditTask,
                                        mutableTaskId = currentTaskId,
                                        deleteState = openDeleteTaskDialog
                                    )
                                }
                            }
                        }

                        is FirestoreState.Success -> {
                            Toast.makeText(
                                context,
                                (firestoreState.value as FirestoreState.Error).message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {}
                    }
                }
            }
        }
    )

    NewTaskDialog(
        openState = openNewTaskDialog,
        firestoreViewModel = firestoreViewModel,
        authViewModel = authViewModel
    )

    EditTaskDialog(
        openState = openEditTaskDialog,
        firestoreViewModel = firestoreViewModel,
        task = toEditTask,
        taskId = currentTaskId
    )

    DeleteTaskDialog(
        openState = openDeleteTaskDialog,
        firestoreViewModel = firestoreViewModel,
        taskId = currentTaskId
    )
}