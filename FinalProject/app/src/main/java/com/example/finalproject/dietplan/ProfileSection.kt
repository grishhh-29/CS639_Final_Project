package com.example.finalproject.dietplan

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import coil.compose.rememberImagePainter
import com.example.finalproject.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ProfileSection : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
        // Check if user is not logged in
        if (!sharedPreferences.getBoolean("is_logged_in", false)) {
            navigateToLogin(this)
            finish()
            return
        }
        /*val sharedPreferences1 = getSharedPreferences("selected_image", Context.MODE_PRIVATE)
        sharedPreferences1.edit {
            putString("latestImageUrl", latestImageUrl)
        }*/
        setContent {
            val currentUser = auth.currentUser
            val currentUserID = currentUser?.uid ?: ""
            val imageUrlState = getImageUrlFromFirestore(currentUserID)
            val latestImageUrl = intent.getStringExtra("latestImageUrl")
            // Save the selected image URL to shared preferences
            val sharedPreferences1 = getSharedPreferences("selected_image", Context.MODE_PRIVATE)
            sharedPreferences1.edit {
                putString("latestImageUrl", latestImageUrl)
            }
            val selectedImageUrl = sharedPreferences1.getString("latestImageUrl", null)
            UserProfileScreen(auth,selectedImageUrl,sharedPreferences)
        }
        }
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        val context = this
        navigateToHomePage(context)
    }
}

@Composable
fun UserProfileScreen(auth:FirebaseAuth, imageUri: String?,sharedPreferences: SharedPreferences) {
    val context = LocalContext.current
    val userEmail = auth.currentUser?.email ?: ""
    // State to hold the latest image URL
    //var latestImageUrl by remember { mutableStateOf<String?>(null) }
    //val selectedImageUrl = latestImageUrl ?: sharedPreferences.getString("latestImageUrl", null)
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.backgroundimg2),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Absolute.Left
            ) {
                ElevatedButton(
                    onClick = {
                        // Navigate back when the back button is clicked
                        navigateToHomePage(context)
                    }
                ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(200.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = CircleShape
                    ) // Border modifier for the circular boundary
            ) {
                Column {
                    imageUri?.let { imageUrl ->
                        // Display the latest image
                            Image(
                                painter = rememberImagePainter(imageUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(CircleShape)
                                    .padding(8.dp),
                                contentScale = ContentScale.Crop
                            )
                        } ?: Image(
                            painter = painterResource(id = R.drawable.applogo1), // Provide the default drawable resource ID
                            contentDescription = null,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                                .padding(8.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Add Upload button
                    OutlinedButton(onClick = { navigateToUpload(context) }) {
                        Text("Upload Image")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("EMAIL: ", modifier = Modifier.padding(end = 8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                        Text(userEmail,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("USER: ", modifier = Modifier.padding(end = 8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                        Text(UserInfo.extractUsernameFromEmail(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Add Reset Password button
                    ElevatedButton(onClick = { navigateToResetPassword(context) }) {
                        Text("Reset Password")
                    }
                    // Add Logout button
                    Spacer(modifier = Modifier.height(16.dp))
                    ElevatedButton(onClick = { logout(context, sharedPreferences) }) {
                        Text("Logout")
                    }
                }
            }
        }
    }
private const val PICK_IMAGE_REQUEST = 1
private fun navigateToResetPassword(context: Context) {
    if (context !is PasswordResetScreen) {
        val intent = Intent(context, PasswordResetScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
        (context as? Activity)?.overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }
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
private fun navigateToUpload(context: Context) {
    val intent = Intent(context, PhotoPick::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}
private fun navigateToHomePage(context: Context) {
    val intent = Intent(context, SuccessActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}
@Composable
private fun getImageUrlFromFirestore(currentUserID: String): String? {
    val db = Firebase.firestore
    val userImagesRef = db.collection("user_images").document(currentUserID)

    var imageUrl: String? = null

    userImagesRef.get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                imageUrl = document.getString("image_url")
            }
        }
        .addOnFailureListener { e ->
            Log.e(TAG, "Error fetching image URL from Firestore", e)
        }

    return imageUrl
}