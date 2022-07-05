package com.example.movieapplication.login_register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.movieapplication.MainActivity
import com.example.movieapplication.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity(), View.OnClickListener {
    private lateinit var email : EditText
    private lateinit var send : Button
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        send = findViewById(R.id.send)
        email = findViewById(R.id.email)

        send.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.send -> {
                var email_ = email.text.toString().trim()

                if(email_.isEmpty()) {
                    email.setError("email is required !")
                    email.requestFocus()
                    return
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email_).matches()){
                    email.setError("please enter an valid email !")
                    email.requestFocus()
                }
                auth.sendPasswordResetEmail(email_).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(applicationContext,"Check your email to reset password", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}