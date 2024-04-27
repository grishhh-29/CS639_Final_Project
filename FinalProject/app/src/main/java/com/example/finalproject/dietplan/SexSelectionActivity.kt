package com.example.finalproject.dietplan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.finalproject.R

class SexSelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            SexSelectionScreen(intent.getStringExtra("goal") ?: "",navController)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val context = this
        navigateToMyDietPlanner(context)
    }
}
@Composable
fun SexSelectionScreen(goal: String,navController: NavController) {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    )
    {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.gender),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            content = {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment =
                    Alignment.Top
                ) {
                    ElevatedButton(
                        onClick = {
                            // Navigate back when the back button is clicked
                            navigateToMyDietPlanner(context)
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
                Text(
                    text = "Please select your sex for $goal",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(20.dp))
                SexSelectionButtons(goal,context)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SexSelectionButtons(goal: String,context:Context) {
    val selectedOption = remember { mutableStateOf("Option1") }
    Row {
        RadioButton(
            selected = selectedOption.value == "Option1",
            onClick = { selectedOption.value = "Option1" },
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Black,
                unselectedColor = Color.Gray
            )
        )
        Text("Male")

        RadioButton(
            selected = selectedOption.value == "Option2",
            onClick = { selectedOption.value = "Option2" },
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Black,
                unselectedColor = Color.Gray
            )
        )
        Text("Female")
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Navigate to the next screen if a sex is selected
                navigateToHeightScreen(context)
            },
            enabled = true, // Enable button only if a sex is selected
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Next")
        }
    }
}

private fun navigateToMyDietPlanner(context: Context) {
    val intent = Intent(context, MyDietPlanner::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}
private fun navigateToHeightScreen(context: Context) {
    val intent = Intent(context, HeightScreen::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}
