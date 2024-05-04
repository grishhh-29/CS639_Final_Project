package com.example.finalproject.dietplan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.core.content.edit
import com.example.finalproject.R
import com.google.firebase.auth.FirebaseAuth
import com.example.finalproject.dietplan.BottomMenu


class PasswordResetScreen : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
        setContent {
            PasswordResetScreen(auth,sharedPreferences)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val context = this
        navigateToSuccessActivity(context)
    }
}
@Composable
fun PasswordResetScreen(auth: FirebaseAuth,sharedPreferences: SharedPreferences) {
    val context = LocalContext.current
    val errMessage = remember { mutableStateOf("") }
    val errMsg = remember { mutableStateOf("") }
    val currentPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val confirmNewPassword = remember { mutableStateOf("") }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.resetpassword),
            contentDescription = "Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment =
            Alignment.Top
        ) {
            ElevatedButton(
                onClick = {
                    // Navigate back when the back button is clicked
                    navigateToLogin(context)
                }
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }

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

                var passwordVis by remember { mutableStateOf(false) }
                TextField(
                    value = currentPassword.value,
                    onValueChange = {
                        currentPassword.value = it
                    },
                    label = { BasicText(text = "Current Password") },
                    visualTransformation = if (passwordVis) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVis = !passwordVis }) {
                            Icon(
                                painter = painterResource(id = if (passwordVis) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                contentDescription = if (passwordVis) "Hide Password" else "Show Password"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                var passwordVisibility by remember { mutableStateOf(false) }
                TextField(
                    value = newPassword.value,
                    onValueChange = {
                        newPassword.value = it
                        if (currentPassword.value == it) {
                            errMsg.value = "New Password is same as current Password"
                        } else {
                            errMsg.value = ""
                        }
                    },
                    label = { BasicText(text = "New Password") },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(
                                painter = painterResource(id = if (passwordVisibility) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                contentDescription = if (passwordVisibility) "Hide Password" else "Show Password"
                            )
                        }
                    },
                    isError = errMsg.value.isNotEmpty(),
                    singleLine = true
                )
                if (errMsg.value.isNotEmpty()) {
                    BasicText(text = errMsg.value)
                }

                Spacer(modifier = Modifier.height(16.dp))
                var passwordVisible by remember { mutableStateOf(false) }
                TextField(
                    value = confirmNewPassword.value,
                    onValueChange = {
                        confirmNewPassword.value = it
                        if (newPassword.value != it) {
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
                    onClick = {
                        updatePassword(
                            auth,
                            newPassword.value,
                            context,
                            sharedPreferences
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                ) {
                    Text("Reset Password")
                }
            }
        }
        BottomMenu(
            items = listOf(
                BottomMenuContent("Home", R.drawable.ic_home),
                BottomMenuContent("Support", R.drawable.ic_bubble),
                BottomMenuContent("Profile", R.drawable.ic_profile)
            ),
            modifier = Modifier.align(Alignment.BottomCenter),
            initialSelectedItemIndex = 1
        )
    }
}

private fun updatePassword(auth: FirebaseAuth, newPassword: String,context: Context,sharedPreferences: SharedPreferences) {
    val user = auth.currentUser
    user?.updatePassword(newPassword)?.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // Password updated successfully
            Toast.makeText(context, "Password updated successfully, LOGIN AGAIN", Toast.LENGTH_SHORT).show()
            // Navigate to login screen
            sharedPreferences.edit {
                putBoolean("is_logged_in", false)
            }
            navigateToLogin(context
            )
        } else {
            // Password update failed
            Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT).show()
        }
    }
}
private fun navigateToLogin(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent)
}
private fun navigateToSuccessActivity(context: Context) {
    val intent = Intent(context, SuccessActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}
