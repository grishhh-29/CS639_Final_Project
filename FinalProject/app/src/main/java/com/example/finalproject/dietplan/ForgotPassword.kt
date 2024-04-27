package com.example.finalproject.dietplan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.safetynet.SafetyNet
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.finalproject.R

class ForgotPassword : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContent {
            ForgotPasswordScreen(auth)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val context = this
        navigateToLoginBack(context)
    }
}

@Composable
fun ForgotPasswordScreen(auth: FirebaseAuth) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var captchaToken by remember { mutableStateOf<String?>(null) }
    var isCaptchaVerified by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.forgotpass),
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
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = "Enter your email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        SafetyNet.getClient(context)
                            .verifyWithRecaptcha("6Lf5wawpAAAAAPKOUD6OFuLMENPhHz57ZK-P7Q0-")
                            .addOnSuccessListener { recaptchaTokenResponse ->
                                val token = recaptchaTokenResponse.tokenResult
                                captchaToken = token
                                isCaptchaVerified = !token.isNullOrEmpty()
                                if (isCaptchaVerified) {
                                    resetPassword(auth, context, email)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please complete the reCAPTCHA verification",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    context,
                                    "Failed to load reCAPTCHA: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    },
                    enabled = true, // Enable button by default
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Reset Password")
                }
            }
        }
    }
}

fun resetPassword(auth: FirebaseAuth, context: Context, email: String) {
    auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Password reset email sent successfully.", Toast.LENGTH_SHORT).show()
                navigateToLogin(context)
            } else {
                Toast.makeText(context, "Failed to send password reset email.", Toast.LENGTH_SHORT).show()
            }
        }
}
private fun navigateToLoginBack(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}


private fun navigateToLogin(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}