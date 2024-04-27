package com.example.finalproject.dietplan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.finalproject.R

class MyDietPlanner : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyDietPlannerScreen()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val context = this
        navigateToSuccessActivity(context)
    }
}

@Composable
fun MyDietPlannerScreen() {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // First Lottie animation
            val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie4))
            // Load Lottie animation
            LottieAnimation(
                modifier = Modifier
                    .height(500.dp),
                composition = composition.value,
                contentScale = ContentScale.FillBounds,
                alignment = Alignment.Center
            )
            //Spacer(modifier = Modifier.height(24.dp))
            // Second Lottie animation
            val composition1 = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie5))
            // Load Lottie animation
            LottieAnimation(
                modifier = Modifier
                    .height(600.dp),
                composition = composition1.value,
                contentScale = ContentScale.FillBounds,
                alignment = Alignment.Center
            )
        }

        // Column for the rest of the content
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            content = {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    ElevatedButton(
                        onClick = {
                            // Navigate back when the back button is clicked
                            navigateToSuccessActivity(context)
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
                Text(
                    text = "What goal do you have in mind?",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        fontSize = 24.sp // Adjust the font size here
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                GoalSelectionCards()
            }
        )
    }
}

@Composable
fun GoalSelectionCards() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        GoalCard("Lose Weight", "This option is for those who want to lose weight")
        Spacer(modifier = Modifier.height(8.dp))
        GoalCard("Maintain Weight", "This option is for those who want to maintain their current weight")
        Spacer(modifier = Modifier.height(8.dp))
        GoalCard("Gain Weight", "This option is for those who want to gain weight")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalCard(goalTitle: String, goalDescription: String) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        onClick = {
            // Navigate to respective goal screen here
            navigateToSexSelection(context, goalTitle)
            Toast.makeText(context, "Redirecting to $goalTitle", Toast.LENGTH_SHORT).show()
            // You'll implement navigation here
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = goalTitle)
            Text(text = goalDescription)
        }
    }
}

private fun navigateToSexSelection(context: Context, goal: String) {
    val intent = Intent(context, SexSelectionActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra("goal", goal)
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}
private fun navigateToSuccessActivity(context: Context) {
    val intent = Intent(context, SuccessActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}