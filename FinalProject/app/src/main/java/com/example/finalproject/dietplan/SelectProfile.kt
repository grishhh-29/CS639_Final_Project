package com.example.finalproject.dietplan

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class SelectProfile : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContent {
            UserProfileScreenSelection(auth)
        }
    }
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        val context = this
        navigateToProfile(context,"")
    }
}
@Composable
fun UserProfileScreenSelection(auth: FirebaseAuth) {
    val currentUser = auth.currentUser
    val context = LocalContext.current
    var latestImageUrl by remember { mutableStateOf<String?>(null) }
    var imageUrls by remember { mutableStateOf<List<String>>(emptyList()) }

    // Fetch image URLs from Firebase Storage
    LaunchedEffect(Unit) {
        val storage = Firebase.storage
        val listRef = storage.reference.child("images")

        val urls = mutableListOf<String>() // Create a mutable list to store URLs

        listRef.listAll()
            .addOnSuccessListener { result ->
                val itemsCount = result.items.size // Total number of items
                var urlsCount = 0 // Counter for collected URLs

                result.items.forEach { item ->
                    item.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        urls.add(imageUrl)
                        urlsCount++ // Increment the count for collected URLs
                        if (urlsCount == itemsCount) {
                            // All URLs collected, update the state
                            imageUrls = urls
                        }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "Errorrrrr!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.backgroundimg),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.End
        ) {
            ElevatedButton(
                onClick = {
                    // Navigate back when the back button is clicked
                    navigateToProfile(context,"")
                }
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Scrollable column to display images
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            imageUrls.forEach { imageUrl ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(200.dp)
                        .clip(CircleShape)
                        .clickable {
                            latestImageUrl = imageUrl // Set selected image as latest image URL
                            currentUser?.uid?.let {
                                saveImageUriToFirestore(
                                    context,
                                    latestImageUrl!!, it
                                )
                            }

                        }
                        .border(
                            width = 2.dp,
                            color = if (latestImageUrl == imageUrl) Color.Black else Color.Transparent, // Highlight selected image
                            shape = CircleShape
                        )
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Button to set profile picture
        Button(
            onClick = {
                latestImageUrl?.let {
                    val currentUserID = currentUser?.uid ?: ""
                    // Save the selected image URL to Firestore for the current user
                    saveImageUriToFirestore(context,latestImageUrl!!, currentUserID)
                    navigateToProfile(context, latestImageUrl!!)
                }
            }
        ) {
            Text("Set Profile Picture")
        }
    }
}
private fun saveImageUriToFirestore(context: Context, imageUrl: String, currentUserID: String) {
    val db = Firebase.firestore
    val userImagesRef = db.collection("user_images").document(currentUserID)

    userImagesRef.set(mapOf("image_url" to imageUrl))
        .addOnSuccessListener {
            Log.d(TAG, "Image URL saved successfully")
            Toast.makeText(
                context,
                "Image URL saved successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
        .addOnFailureListener { e ->
            Log.e(TAG, "Error saving image URL", e)
            Toast.makeText(
                context,
                "Error saving image URL: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
}
/*private fun saveImageUriToSharedPreferences(context: Context, imageUrl: String, currentUserID: String) {
    val sharedPreferences = context.getSharedPreferences("selected_image", Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putString("user_${currentUserID}_image", imageUrl)
    }
}*/
private fun navigateToProfile(context: Context,imageUrl:String) {
    val intent = Intent(context, ProfileSection::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra("latestImageUrl", imageUrl)
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}

