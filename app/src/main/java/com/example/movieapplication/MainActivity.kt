package com.example.movieapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.movieapplication.home.HomeActivity
import com.example.movieapplication.login_register.ForgotPassword
import com.example.movieapplication.login_register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var register: TextView
    private lateinit var forgotPassword: TextView
    private lateinit var login: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register = findViewById(R.id.register)
        login = findViewById(R.id.login)
        forgotPassword = findViewById(R.id.forgotPassword)

        register.setOnClickListener(this)
        login.setOnClickListener(this)
        forgotPassword.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.register -> startActivity(Intent(this, RegisterActivity::class.java))
            R.id.forgotPassword -> startActivity(Intent(this, ForgotPassword::class.java))
            R.id.login -> {
                email = findViewById(R.id.email)
                password = findViewById(R.id.password)

                var email_ = email.text.toString().trim()
                var password_ = password.text.toString().trim()

                if (email_.isEmpty()) {
                    email.setError("email is required !")
                    email.requestFocus()
                    return
                }
                if (password_.isEmpty()) {
                    password.setError("password is required !")
                    password.requestFocus()
                    return
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email_).matches()) {
                    email.setError("please enter an valid email !")
                    email.requestFocus()
                }
                auth.signInWithEmailAndPassword(email_, password_).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Logged in successfully", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(
                        applicationContext,
                        exception.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}