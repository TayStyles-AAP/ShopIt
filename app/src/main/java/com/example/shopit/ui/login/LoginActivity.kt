package com.example.shopit.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shopit.MainActivity
import com.example.shopit.R
import com.example.shopit.ui.reset.ResetActivity
import com.example.shopit.ui.signup.SignupActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    //firebase
    private lateinit var auth: FirebaseAuth

    //Add UI elements as variables with types of the elements
    lateinit var emailTextInput: EditText
    lateinit var passwordTextInput: EditText
    lateinit var loginButton: Button
    lateinit var signupButton: Button
    lateinit var resetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitvity_login)

        //firebase
        auth = Firebase.auth

        //initilise the variables
        emailTextInput = findViewById(R.id.login_email_input)
        passwordTextInput = findViewById(R.id.login_password_input)
        loginButton = findViewById(R.id.login_login_button)
        signupButton = findViewById(R.id.login_signup_button)
        resetButton = findViewById(R.id.login_reset_password_button)

        loginButton.setOnClickListener {
            didClickLogin()
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

    private fun didClickLogin(){
        if (validateEmail() && validatePassword()){
            //transition to home page

            auth.signInWithEmailAndPassword(emailTextInput.text.toString(), passwordTextInput.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed. Please try again later", Toast.LENGTH_SHORT).show()
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

    private fun validatePassword(): Boolean {
        val password = passwordTextInput.text.toString()

        return when {
            password.isEmpty() -> {
                passwordTextInput.error = "Please enter password!"
                false
            }
            password.length <= 8 -> {
                passwordTextInput.error = "Check Password."
                false
            }
            else -> {
                true
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            Log.d(TAG, "User is already logged in (${currentUser.displayName})")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    companion object{
        private const val TAG = "ShopIt-LoginActivity"
    }
}