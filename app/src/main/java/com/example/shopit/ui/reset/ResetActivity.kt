package com.example.shopit.ui.reset

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shopit.MainActivity
import com.example.shopit.R
import com.example.shopit.ui.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ResetActivity : AppCompatActivity() {

    //Add Ui elements as variables with types of elements
    lateinit var emailTextInput: EditText
    lateinit var sendReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        //initilise the variables
        emailTextInput = findViewById(R.id.editTextTextEmailAddressResetPassword)
        sendReset = findViewById(R.id.buttonResetPassword)


        sendReset.setOnClickListener {
            if (validateEmail()) {
                //transition to Reset Password Activity
                Firebase.auth.sendPasswordResetEmail(emailTextInput.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext, "Successful. Check your email.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(baseContext, "Failed to send reset email. Try again later", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun validateEmail(): Boolean {
        val email = emailTextInput.text.toString()

        return if (email.isEmpty()) {
            emailTextInput.error = "Please enter email address!"
            false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextInput.error = "Please check your email!"
            false
        } else {
            true
        }
    }
}