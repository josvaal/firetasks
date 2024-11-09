package com.josval.firetasks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.josval.firetasks.model.Priority
import com.josval.firetasks.model.TaskModel

class TaskViewModel : ViewModel() {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _firestoreState = MutableLiveData<FirestoreState>()
    val firestoreState: LiveData<FirestoreState> get() = _firestoreState

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> get() = _tasks

    fun tasksSnapshot(uid: String) {
        _firestoreState.value = FirestoreState.Loading

        firestore.collection("tasks").whereEqualTo("createdBy", uid)
            .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (error != null) {
                    _firestoreState.value = FirestoreState.Error(error.message ?: "Unknown error")
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val taskList = it.documents.mapNotNull { document ->
                        val title = document.getString("title")
                        val body = document.getString("body")
                        val priority = document.getString("priority") ?: "NONE"
                        if (title != null && body != null) {
                            TaskModel(title, body, Priority.fromString(priority))
                        } else {
                            null
                        }
                    }
                    _tasks.value = taskList
                    _firestoreState.value = FirestoreState.Fetched
                }
            }
    }
}

sealed class FirestoreState {
    object Loading : FirestoreState()
    object Fetched : FirestoreState()
    data class Error(val message: String) : FirestoreState()
}
