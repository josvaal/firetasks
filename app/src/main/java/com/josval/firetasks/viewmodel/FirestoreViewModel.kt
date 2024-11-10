package com.josval.firetasks.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.josval.firetasks.model.Priority
import com.josval.firetasks.model.TaskModel

class FirestoreViewModel : ViewModel() {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _firestoreState = MutableLiveData<FirestoreState>()
    val firestoreState: LiveData<FirestoreState> get() = _firestoreState

    private val _tasks = MutableLiveData<List<Pair<String, TaskModel>>>()
    val tasks: LiveData<List<Pair<String, TaskModel>>> get() = _tasks

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    private val tasksPath: CollectionReference = firestore.collection("tasks")

    fun tasksSnapshot(uid: String) {
        _firestoreState.value = FirestoreState.Loading

        tasksPath.whereEqualTo("createdBy", uid)
            .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (error != null) {
                    _firestoreState.value = FirestoreState.Error(error.message ?: "Algo salió mal")
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val taskList = it.documents.mapNotNull { document ->
                        val title = document.getString("title")
                        val body = document.getString("body")
                        val priority = document.getString("priority") ?: "NONE"
                        val createdBy = document.getString("createdBy") ?: "NONE"
                        val id = document.id

                        if (title != null && body != null) {
                            val task =
                                TaskModel(title, body, Priority.fromString(priority), createdBy)
                            Pair(id, task)
                        } else {
                            null
                        }
                    }.sortedBy { pair ->
                        when (pair.second.priority) {
                            Priority.HIGH -> 1
                            Priority.MEDIUM -> 2
                            Priority.LOW -> 3
                            Priority.NONE -> 4
                        }
                    }
                    _tasks.value = taskList
                    _firestoreState.value = FirestoreState.Fetched
                }
            }
    }

    fun createTask(task: TaskModel, uid: String) {
        _firestoreState.value = FirestoreState.Loading
        tasksPath.add(task)
            .addOnSuccessListener {
                _toastMessage.value = "¡Tarea creada exitosamente!"
                _firestoreState.value = FirestoreState.Fetched
            }
            .addOnFailureListener { error ->
                _toastMessage.value = error.message ?: "Algo salió mal"
            }
    }

    fun deleteTask(id: String) {
        _firestoreState.value = FirestoreState.Loading
        tasksPath.document(id).delete()
            .addOnSuccessListener {
                _firestoreState.value = FirestoreState.Fetched
                _toastMessage.value = "¡Tarea eliminada exitosamente!"
            }
            .addOnFailureListener { error ->
                _toastMessage.value = error.message ?: "Algo salió mal"
            }
    }

    fun updateTask(task: TaskModel, id: MutableState<String>) {
        _firestoreState.value = FirestoreState.Loading
        tasksPath.document(id.value).update(
            mapOf(
                "title" to task.title,
                "body" to task.body,
                "priority" to task.priority
            )
        ).addOnSuccessListener {
            _firestoreState.value = FirestoreState.Fetched
            _toastMessage.value = "¡Tarea actualizada exitosamente!"
        }.addOnFailureListener { error ->
            _toastMessage.value = error.message ?: "Algo salió mal"
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

}

sealed class FirestoreState {
    data class Success(val message: String) : FirestoreState()
    object Loading : FirestoreState()
    object Fetched : FirestoreState()
    data class Error(val message: String) : FirestoreState()
}
