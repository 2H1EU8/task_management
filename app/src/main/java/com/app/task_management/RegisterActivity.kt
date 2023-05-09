package com.app.task_management


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;

    private lateinit var emailTxt: EditText
    private lateinit var passTxt: EditText
    private lateinit var repassTxt: EditText

    private lateinit var signupBtn: Button
    private lateinit var redirectBtn : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        emailTxt = findViewById(R.id.sigup_email)
        passTxt = findViewById(R.id.signup_pass)
        repassTxt = findViewById(R.id.signup_repass)

        signupBtn = findViewById(R.id.signin_btn)
        redirectBtn = findViewById(R.id.goto_signin)

        //event
        signupBtn.setOnClickListener{
            val email  = emailTxt.text.toString().trim()
            val pass = passTxt.text.toString().trim()
            val repass = repassTxt.text.toString().trim()

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
                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { it ->
                        if(it.isSuccessful){
                            Toast.makeText(this, "Sign up success!",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                        else{
                            Toast.makeText(this, "Sign up fail! "+ it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }
        redirectBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}