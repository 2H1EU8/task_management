package com.app.task_management.viewmodel

import androidx.lifecycle.ViewModel
import com.app.task_management.data.Project
import com.app.task_management.util.Constant
import com.app.task_management.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private var db: FirebaseFirestore
) :ViewModel(){

    private val _task = MutableStateFlow<Resource<Project>>(Resource.Unspecified())
    val task : Flow<Resource<Project>> = _task

    fun addProject(project: Project) {
        //send state
        runBlocking {
            _task.emit(Resource.Loading())
        }
        db.collection(Constant.USER_COLLECTION)
            .document(auth.uid!!)
            .collection(Constant.PROJECT_COLLECTION)
            .add(project)
            .addOnSuccessListener {
                _task.value = Resource.Success(project)
            }
            .addOnFailureListener {
                _task.value = Resource.Error(it.message.toString())
            }
    }
}