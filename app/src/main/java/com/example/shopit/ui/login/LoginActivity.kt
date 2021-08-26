package com.example.shopit.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.shopit.MainActivity
import com.example.shopit.R
import com.example.shopit.ui.reset.ResetActivity
import com.example.shopit.ui.SignupActivity

class LoginActivity : AppCompatActivity() {

    //Add UI elements as variables with types of the elements
    lateinit var emailTextInput: EditText
    lateinit var passwordTextInput: EditText
    lateinit var loginButton: Button
    lateinit var signupButton: Button
    lateinit var resetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitvity_login)

        //initilise the variables
        emailTextInput = findViewById(R.id.login_email_input)
        passwordTextInput = findViewById(R.id.login_password_input)
        loginButton = findViewById(R.id.login_login_button)
        signupButton = findViewById(R.id.login_signup_button)
        resetButton = findViewById(R.id.login_reset_password_button)

        loginButton.setOnClickListener {
            if (validateEmail() && validatePassword()){
                //transition to home page
                startActivity(Intent(this, MainActivity::class.java))
                finish() //This means that if the user pressed the back button on the OS, it will not be able to navigate back to this screen
            }
        }

        signupButton.setOnClickListener {
            //transition to signup screen
            startActivity(Intent(this, SignupActivity::class.java))
            //note how these dont say finish, this is so the user can press back and be returned to the login screen.
        }

        resetButton.setOnClickListener {
            //transition to reset screen
            startActivity(Intent(this, ResetActivity::class.java))
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

    private fun validatePassword(): Boolean {
        val password = passwordTextInput.text.toString()

        return when {
            password.isEmpty() -> {
                passwordTextInput.error = "Please enter password!"
                false
            }
            password.length < 6 -> {
                passwordTextInput.error = "Please enter check your password!"
                false
            }
            else -> {
                true
            }
        }
    }
}