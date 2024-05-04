package com.example.finalproject.dietplan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finalproject.dietplan.view.BottomBar
import com.example.finalproject.dietplan.view.Home
import com.example.finalproject.dietplan.view.MultiplePhotoPicker
import com.example.finalproject.dietplan.view.SinglePhotoPicker
import com.example.finalproject.dietplan.view.SingleVideoPicker
import com.example.finalproject.ui.theme.PhotoPickerTheme

class PhotoPick : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoPickerTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "single") {
                    composable("home") {
                        Scaffold(
                            bottomBar = {
                                BottomBar(
                                    navController = navController
                                )
                            },
                        ) { contentPadding ->
                            Home()
                        }
                    }
                    composable("single") {
                        Scaffold(
                            bottomBar = {
                                BottomBar(
                                    navController = navController
                                )
                            },
                        ) { contentPadding ->
                            SinglePhotoPicker()
                        }
                    }
                    composable("multi") {
                        Scaffold(
                            bottomBar = {
                                BottomBar(navController = navController)
                            },
                        ){ contentPadding ->
                            MultiplePhotoPicker()
                        }
                    }

                    composable("video") {
                        Scaffold(
                            bottomBar = {
                                BottomBar(navController = navController)
                            },
                        ){ contentPadding ->
                            SingleVideoPicker()
                        }
                    }
                }
            }
        }
    }
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        val context = this
        navigateToProfile(context)
    }
}
private fun navigateToProfile(context: Context) {
    val intent = Intent(context, ProfileSection::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
}