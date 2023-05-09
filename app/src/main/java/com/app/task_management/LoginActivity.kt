package com.app.task_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;

    private lateinit var emailTxt: EditText
    private lateinit var passTxt: EditText

    private lateinit var signingBtn: Button
    private lateinit var redirectBtn : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

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
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { it ->
                if(it.isSuccessful) {
                    Toast.makeText(this, "Welcome to tasker", Toast.LENGTH_SHORT).show()

                    //Move on to main
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }else {
                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
        redirectBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}