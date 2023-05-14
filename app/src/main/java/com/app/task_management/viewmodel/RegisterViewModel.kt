package com.app.task_management.viewmodel

import androidx.lifecycle.ViewModel
import com.app.task_management.data.User
import com.app.task_management.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private var db: FirebaseFirestore
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register : Flow<Resource<User>> = _register
    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String, repass: String){
        if(check(user, password, repass)) {
        runBlocking {
            _register.emit(Resource.Loading())
        }
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener {
                it.user?.let {
                    saveUserInfo(it.uid.toString(), user)
                }
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
        }else{
            val registerFieldState = RegisterFieldState(
                emailValidation(user.email), passValidation(password,repass)
            )

            runBlocking {
                _validation.send(registerFieldState)
            }
        }
    }

    private fun saveUserInfo(id: String, user: User) {
        db.collection(Constant.USER_COLLECTION)
            .document(id)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)

            }.addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())

            }
    }


    //validate
    private fun check(
        user: User,
        password: String,
        repass: String
    ): Boolean {
        val emailValidation = emailValidation(user.email)
        val passValidation = passValidation(password, repass)

        return emailValidation is RegisterValidation.Success &&
                passValidation is RegisterValidation.Success
    }
}