package com.example.finalproject.dietplan.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.finalproject.dietplan.PhotoPick
import com.example.finalproject.dietplan.SelectProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class StorageUtil {


    companion object {

        fun uploadToStorage(uri: Uri, context: Context, type: String) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val currentUserID = currentUser?.uid
            val storage = Firebase.storage

            // Create a storage reference from our app
            var storageRef = storage.reference

            val unique_image_name = UUID.randomUUID()
            var spaceRef: StorageReference

            if (type == "image") {
                spaceRef = storageRef.child("images/$unique_image_name.jpg")
            } else {
                spaceRef = storageRef.child("videos/$unique_image_name.mp4")
            }

            val byteArray: ByteArray? = context.contentResolver
                .openInputStream(uri)
                ?.use { it.readBytes() }

            byteArray?.let {

                var uploadTask = spaceRef.putBytes(byteArray)
                uploadTask.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "upload failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Handle unsuccessful uploads
                }.addOnSuccessListener { taskSnapshot ->
                    Toast.makeText(
                        context,
                        "upload success",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToProfileSelection(context)
                }
            }

        }
        private fun navigateToProfileSelection(context: Context) {
            val intent = Intent(context, SelectProfile::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }
}

