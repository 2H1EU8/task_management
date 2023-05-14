package com.app.task_management.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.app.task_management.R
import com.app.task_management.util.Resource
import com.app.task_management.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var emailTxt: EditText
    private lateinit var passTxt: EditText

    private lateinit var signingBtn: CircularProgressButton
    private lateinit var redirectBtn : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailTxt = findViewById(R.id.sigin_email)
        passTxt = findViewById(R.id.signin_pass)
        signingBtn = findViewById(R.id.signin_btn)
        redirectBtn = findViewById(R.id.goto_signup)

        //event
        signingBtn.setOnClickListener {
            val email = emailTxt.text.toString().trim()
            val pass = passTxt.text.toString().trim()

            if(email.isEmpty()) {
                emailTxt.error = "Email cannot be empty"
                return@setOnClickListener
            }
            if(pass.isEmpty()) {
                passTxt.error = "Password cannot be empty"
                return@setOnClickListener
            }
            viewModel.loginWithEmailAndPassword(email, pass)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect{
                when(it) {
                    is Resource.Loading -> {
                        signingBtn.startAnimation()
                    }
                    is Resource.Success -> {
                        signingBtn.revertAnimation()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        //clear in callstack, avoid back to login
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                    is Resource.Error -> {
                        Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_LONG).show()
                        signingBtn.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }

        redirectBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))

        }
    }
}