package com.example.movieapplication.login_register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.movieapplication.MainActivity
import com.example.movieapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var login: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var register: Button
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        login = findViewById(R.id.login)
        register = findViewById(R.id.register)

        login.setOnClickListener(this)
        register.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login -> startActivity(Intent(this, MainActivity::class.java))
            R.id.register -> {
                name = findViewById(R.id.name)
                email = findViewById(R.id.email)
                password = findViewById(R.id.password)

                var name_ = name.text.toString().trim()
                var email_ = email.text.toString().trim()
                var password_ = password.text.toString().trim()

                if (name_.isEmpty()) {
                    name.setError("name is required !")
                    name.requestFocus()
                    return
                }
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
                auth.createUserWithEmailAndPassword(email_, password_)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            user = User(name_, email_)
                            FirebaseDatabase.getInstance().getReference("users")
                                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user)
                            Toast.makeText(
                                applicationContext,
                                "Signed up successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(Intent(this, MainActivity::class.java))
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