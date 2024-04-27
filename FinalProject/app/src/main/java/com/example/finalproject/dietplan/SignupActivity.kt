package com.example.finalproject.dietplan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.Toast
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignUpActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        setContent {
            SignUpScreen(auth)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val context = this
        navigateToLogin(context)
    }
}
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun SignUpScreen(auth:FirebaseAuth) {
        val messageVal = remember { mutableStateOf("") }
        val errMessage = remember { mutableStateOf("") }
        val emailState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }
        val confirmPasswordState = remember { mutableStateOf("") }
        val context = LocalContext.current

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background image
            Image(
                painter = painterResource(id = R.drawable.getstarted),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = emailState.value,
                        onValueChange = { emailState.value = it },
                        label = { BasicText(text = "Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    var passwordVisibility by remember { mutableStateOf(false) }
                    TextField(
                        value = passwordState.value,
                        onValueChange = {
                            passwordState.value = it
                        },
                        label = { BasicText(text = "Password") },
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                Icon(
                                    painter = painterResource(id = if (passwordVisibility) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                    contentDescription = if (passwordVisibility) "Hide Password" else "Show Password"
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    var passwordVisible by remember { mutableStateOf(false) }
                    TextField(
                        value = confirmPasswordState.value,
                        onValueChange = {
                            confirmPasswordState.value = it
                            if (passwordState.value != it) {
                                errMessage.value = "Passwords do not match."
                            } else {
                                errMessage.value = ""
                            }
                        },
                        label = { BasicText(text = "Confirm Password") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                    contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                                )
                            }
                        },
                        isError = errMessage.value.isNotEmpty(),
                        singleLine = true // Add this line to ensure it's a single line TextField
                    )
                    if (errMessage.value.isNotEmpty()) {
                        BasicText(text = errMessage.value)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { signUp(auth,context, emailState.value, passwordState.value, confirmPasswordState.value, messageVal) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        BasicText(text = "Sign Up")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Make the "Already have an account? Log in" text clickable
                    Text(
                        text = "Already a User? Login",
                        modifier = Modifier.clickable {
                            navigateToLogin(context)
                        },
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )

                    if (messageVal.value.isNotEmpty()) {
                        BasicText(text = messageVal.value)
                    }
                }
            }
        }
    }


private fun signUp(auth: FirebaseAuth, context: Context, email: String, password: String, confirmPassword: String, messageVal: MutableState<String>) {
    if (password != confirmPassword) {
        messageVal.value = "Passwords do not match. Please try again."
        return
    }

    // Create the user directly and handle the specific error cases
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { createUserTask ->
            if (createUserTask.isSuccessful) {
                // Sign up success
                Toast.makeText(context, "Signup successful. Now Redirecting", Toast.LENGTH_SHORT).show()
                navigateToLogin(context)
            } else {
                // Sign up failed, check the specific error
                val exception = createUserTask.exception
                if (exception is FirebaseAuthUserCollisionException) {
                    Toast.makeText(context, "User with this email already exists.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Sign up failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
}
private fun navigateToLogin(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}




