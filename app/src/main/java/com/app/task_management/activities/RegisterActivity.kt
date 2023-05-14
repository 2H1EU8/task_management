package com.app.task_management.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.app.task_management.R
import com.app.task_management.data.User
import com.app.task_management.util.RegisterValidation
import com.app.task_management.util.Resource
import com.app.task_management.viewmodel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

private const val TAG = "Register Activity"
@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var emailTxt: EditText
    private lateinit var passTxt: EditText
    private lateinit var repassTxt: EditText
    private lateinit var fNameTxt: EditText
    private lateinit var lNameTxt: EditText

    private lateinit var signupBtn : CircularProgressButton
    private lateinit var redirectBtn : TextView

    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailTxt = findViewById(R.id.sigup_email)
        passTxt = findViewById(R.id.signup_pass)
        repassTxt = findViewById(R.id.signup_repass)
        fNameTxt = findViewById(R.id.fname)
        lNameTxt = findViewById(R.id.lname)

        signupBtn = findViewById(R.id.signin_btn)
        redirectBtn = findViewById(R.id.goto_signin)


        //event
        signupBtn.setOnClickListener{
            val email  = emailTxt.text.toString().trim()
            val pass = passTxt.text.toString().trim()
            val repass = repassTxt.text.toString().trim()
            val firsName = fNameTxt.text.toString().trim()
            val lastName = lNameTxt.text.toString().trim()

            if(email.isEmpty()) {
                emailTxt.error = "Email cannot be empty"
                return@setOnClickListener
            }
            if(pass.isEmpty()) {
                passTxt.error = "Password cannot be empty"
                return@setOnClickListener
            }
            if(repass.isEmpty()) {
                repassTxt.error = "Re-enter password pleas!"
                return@setOnClickListener
            }
            if(repass != pass) {
                passTxt.error = "Password not match"
                repassTxt.error = "Password not match"
                return@setOnClickListener
            }
            else {
                val user = User(
                    firsName, lastName, email
                )
                viewModel.createAccountWithEmailAndPassword(user, pass, repass)
            }
        }
        //handle button state
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect {
                when(it) {
                    is Resource.Loading -> {
                        signupBtn.startAnimation()
                    }
                    is Resource.Success -> {
                        Log.d("Test", it.data.toString())
                        signupBtn.revertAnimation()
                    }

                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        signupBtn.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }
        //handle register validation
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect{ validation ->
                //change edittext state
                if(validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        emailTxt.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                //change edittext state
                if(validation.pass is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        passTxt.apply {
                            requestFocus()
                            error = validation.pass.message
                        }
                        repassTxt.apply {
                            requestFocus()
                            error = validation.pass.message
                        }
                    }
                }
            }
        }


        redirectBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}