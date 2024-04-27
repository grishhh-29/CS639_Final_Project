package com.example.finalproject.dietplan;

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import coil.compose.rememberImagePainter
import com.example.finalproject.R
import com.example.finalproject.dietplan.BottomMenu
import com.example.finalproject.dietplan.BottomMenuContent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class UserProfile : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserProfileScreen(sharedPreferences)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onActivityResult(requestCode, resultCode, data) { imageUri ->
            // Handle the selected image URI here
        }
    }
}

@Composable
fun UserProfileScreen(sharedPreferences: SharedPreferences) {
    val context = LocalContext.current
    val imageUri by remember { mutableStateOf<Uri?>(null) }
    val contact = "vs42370n@pace.edu"
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image View
        ProfileImage(imageUri)

        Spacer(modifier = Modifier.height(16.dp))

        // Button for Image Selection
        ElevatedButton(onClick = { openImagePicker(context) }) {
            Text("Upload Photo")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Contact field (pre-populated)
        Text(
            text = "Contact: $contact",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Reset Password",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navigateToResetPassword(context)
                }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ElevatedButton(onClick = { logout(context, sharedPreferences) }) {
            Text("Upload Photo")
        }

    }
        BottomMenu(
            items = listOf(
                BottomMenuContent("Home", R.drawable.ic_home),
                BottomMenuContent("Music", R.drawable.ic_music),
                BottomMenuContent("Profile", R.drawable.ic_profile)
            ),
            modifier = Modifier.align(Alignment.BottomCenter),
            initialSelectedItemIndex = 2
        )
    }
}

@Composable
fun ProfileImage(imageUri: Uri?) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape)
    ) {
        if (imageUri != null) {
            Image(
                painter = rememberImagePainter(imageUri),
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.addimg),
                contentDescription = "Profile Picture Placeholder",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

fun openImagePicker(context: Context) {
    if (context is Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        context.startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
}



fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, callback: (Uri?) -> Unit) {
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
        val imageUri: Uri = data.data!!
        callback(imageUri)
    }
}

private const val PICK_IMAGE_REQUEST = 1

private fun navigateToResetPassword(context: Context) {
    val intent = Intent(context, PasswordResetScreen::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}
private fun logout(context: Context, sharedPreferences: SharedPreferences) {
    val auth = FirebaseAuth.getInstance()

    // Sign out the user from Firebase
    auth.signOut()

    // Revoke access token from Google
    val googleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)
    googleSignInClient.revokeAccess().addOnCompleteListener {
        // Update login status
        sharedPreferences.edit {
            putBoolean("is_logged_in", false)
        }

        // Navigate to login activity
        navigateToLogin(context)
    }
}
private fun navigateToLogin(context: Context) {
    val intent = Intent(context, LoginActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
}