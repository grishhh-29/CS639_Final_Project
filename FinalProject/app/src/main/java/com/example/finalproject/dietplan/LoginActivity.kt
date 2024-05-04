package com.example.finalproject.dietplan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.example.finalproject.R
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
        // Check if user is already logged in
        if (sharedPreferences.getBoolean("is_logged_in", false)) {
                navigateToSuccessActivity(this)
            finish()
            return
        }

        setContent {
                LoginScreen(auth, sharedPreferences)
        }
    }
}
@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreen(auth: FirebaseAuth,sharedPreferences: SharedPreferences) {
    val errorMessage = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val context = LocalContext.current
    val images = listOf(R.drawable.well2, R.drawable.well1, R.drawable.well3)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier
                .weight(1f)
        ) {
            items(images.size) { index ->
                Image(
                    painter = painterResource(id = images[index]),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { BasicText(text = "Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            var passwordVisibility by remember { mutableStateOf(false) }
            TextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { BasicText(text = "Password") },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
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
            ElevatedButton(
                onClick = {
                    login(
                        auth,
                        context,
                        emailState.value,
                        passwordState.value,
                        sharedPreferences
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicText(text = "Login")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Center
            ) {
                // Forgot Password button aligned to the right
                Button(
                    onClick = {
                        navigateToForgotPassword(context)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = "Forgot Password?",color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Make the "New User? Signup" text clickable
            Text(
                text = "Don't have an account? Sign up",
                modifier = Modifier.clickable {
                    navigateToSignUp(context)
                },
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Make the "New User? Signup" text clickable
            Text(
                text = "OR",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                textAlign = TextAlign.Center
            )
            if (errorMessage.value.isNotEmpty()) {
                BasicText(text = errorMessage.value)
            }
            // Google Sign-In button
            Spacer(modifier = Modifier.height(16.dp))
            ElevatedButton(
                onClick = {
                    navigateLoginWithGoogle(context)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicText(text = "Sign in with Google")
            }
        }
    }
}
private fun login(auth: FirebaseAuth,context: Context, email: String, password: String,sharedPreferences: SharedPreferences) {
    auth.fetchSignInMethodsForEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result?.signInMethods ?: emptyList<String>()
                if (result.contains("google.com")) {
                    // User has signed in with Google, show error message
                    Toast.makeText(context, "Please sign in with Google", Toast.LENGTH_SHORT).show()
                } else {
                    // Proceed with email/password authentication
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Successfully signed in with email/password
                                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                    sharedPreferences.edit {
                                        putBoolean("is_logged_in", true)
                                    }
                                    navigateToSuccessActivity(context)
                            }else {
                                // Handle sign-in failure
                                Toast.makeText(context, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            } else {
                // Handle error fetching sign-in methods
                Toast.makeText(context, "Error fetching sign-in methods. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
}

private fun navigateToSignUp(context: Context) {
    val intent = Intent(context, SignUpActivity::class.java)
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}
private fun navigateToSuccessActivity(context: Context) {
    val intent = Intent(context, SuccessActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}
private fun navigateToForgotPassword(context: Context) {
    val intent = Intent(context, ForgotPassword::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}
private fun navigateLoginWithGoogle(context: Context) {
    val intent = Intent(context, loginWithGoogle::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}


