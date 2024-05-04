package com.example.finalproject.dietplan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalproject.R

class CategorizeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val weight = intent.getDoubleExtra("WEIGHT", 0.0)
        val height = intent.getDoubleExtra("HEIGHT", 0.0)
        super.onCreate(savedInstanceState)
        setContent {
            CategoryScreen(this,weight,height)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val context = this
        navigateToDietActivity(context)
    }
}
@Composable
fun CategoryScreen(context: Context, weight: Double, height: Double) {
    //var timeFrame by remember { mutableStateOf(TextFieldValue()) }
    var targetCalories by remember { mutableStateOf(TextFieldValue()) }
    val radioOptions = listOf("day","week")
    val (timeFrame, onOptionSelected) = remember { mutableStateOf(radioOptions[1] ) }
    val radioOptions1 = listOf("Whole30","primal","paleo","vegan","vegatarian","Ketogenic")
    val (diet, onOptionSel) = remember { mutableStateOf(radioOptions1[1] ) }
    Box(modifier = Modifier.fillMaxSize()) {
        // Add your background image
        Image(
            painter = painterResource(id = R.drawable.food1),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
            //verticalArrangement = Arrangement.Center,
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "TimeFrame:",
                modifier = Modifier.padding(16.dp),
                color = Color.Black,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            Column {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (text == timeFrame),
                                onClick = {
                                    onOptionSelected(text)
                                }
                            )
                            .padding(horizontal = 16.dp)
                    ) {
                        RadioButton(
                            selected = (text == timeFrame),
                            onClick = { onOptionSelected(text) }
                        )
                        Text(
                            text = text,
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            TextField(
                value = targetCalories.text,
                //onValueChange = {targetCalories  = TextFieldValue(it) },
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.toDoubleOrNull() != null) {
                        if (newValue.indexOf('.') != 0) { // Dot should not be at first place
                            targetCalories = TextFieldValue(newValue)
                        }
                    }
                },
                label = {
                    Text(
                        "Target Calories",
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Text(
                text = "Diet Type:",
                modifier = Modifier.padding(16.dp),
                color = Color.Black,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            LazyColumn {
                items(radioOptions1) { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (text == diet),
                                onClick = {
                                    onOptionSel(text)
                                }
                            )
                            .padding(horizontal = 16.dp)
                    ) {
                        RadioButton(
                            selected = (text == diet),
                            onClick = { onOptionSel(text) }
                        )
                        Text(
                            text = text,
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }

            val calories = targetCalories.text.toIntOrNull() ?: 0
            Log.d("TAG", "Target Calories: $calories")
            Log.d("TAG", "diet: $diet")
            Log.d("TAG", "Time: $timeFrame")
            ElevatedButton(
                onClick = {
                    navigatetoPlanMealScreen(context, timeFrame, calories, diet, weight, height)
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Generate Diet Plan")
            }
    }

// Function to navigate to the next screen
fun navigatetoPlanMealScreen(
    context: Context,
    timeFrame: String,
    targetCalories: Int,
    diet: String,
    weight: Double,
    height: Double
) {
    val intent = Intent(context, PlanMealScreen::class.java).apply {
        putExtra("TIME_FRAME", timeFrame)
        putExtra("TARGET_CALORIES", targetCalories)
        putExtra("DIET_TYPE", diet)
        putExtra("WEIGHT", weight)
        putExtra("HEIGHT", height)
    }
    context.startActivity(intent)
}

private fun navigateToDietActivity(context: Context) {
    val intent = Intent(context, DietActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}