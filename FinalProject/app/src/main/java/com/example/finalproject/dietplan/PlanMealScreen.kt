package com.example.finalproject.dietplan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.finalproject.R

class PlanMealScreen : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var apiService: SpoonacularApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val timeFrame = intent.getStringExtra("TIME_FRAME") ?: "day"
            val targetCalories = intent.getIntExtra("TARGET_CALORIES", 0)
            val diet = intent.getStringExtra("DIET_TYPE") ?: "vegan"
            //DietScreen(this)
            // Initialize Retrofit service
            apiService = MealPlanner.instance
            // Call generateMealPlan() when the activity is created
            if(timeFrame == "day"){
                DietActivity1(apiService,timeFrame,targetCalories,diet)
            }
            else{
                DietActivity(apiService,timeFrame,targetCalories,diet)
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        val context = this
        navigateToSexSelection(context)
    }
}

@Composable
fun DietActivity(apiService: SpoonacularApiService, timeFrame: String, targetCalories: Int, diet: String) {
    // Make the API call using Retrofit
    val mealPlanState = remember { mutableStateOf<MealNutrients?>(null) }
    val apiKey = "0e028831e9eb4b669658453b6d14eb8b"
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        apiService.generateMealPlan(timeFrame, targetCalories,diet, apiKey).enqueue(object : Callback<MealNutrients> {
            override fun onResponse(call: Call<MealNutrients>, response: Response<MealNutrients>) {
                if (response.isSuccessful) {
                    val mealPlan = response.body()
                    Log.d("TAG", "mealPlan1+++: ${mealPlan}")
                    // Update the meal plan state
                    mealPlanState.value = mealPlan
                } else {
                    Toast.makeText(context, "Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MealNutrients>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    val mealPlan = mealPlanState.value
    if (mealPlan != null) {
        MealPlanScreen(mealPlan)
    } else {
        Toast.makeText(context, "Failed to fetch meal plan. Please try again.", Toast.LENGTH_SHORT).show()
    }
}
@Composable
fun DietActivity1(apiService: SpoonacularApiService, timeFrame: String, targetCalories: Int, diet: String) {
    // Make the API call using Retrofit
    val mealPlanState = remember { mutableStateOf<Day?>(null) }
    val apiKey = "0e028831e9eb4b669658453b6d14eb8b"
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        apiService.generateMealPlanbyDay(timeFrame, targetCalories,diet, apiKey).enqueue(object : Callback<Day> {
            override fun onResponse(call: Call<Day>, response: Response<Day>) {
                if (response.isSuccessful) {
                    val mealPlan = response.body()
                    // Update the meal plan state
                    mealPlanState.value = mealPlan
                    Log.d("TAG", "mealPlanState+++: ${mealPlanState.value}")
                } else {
                    Toast.makeText(context, "Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Day>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    val mealPlan = mealPlanState.value
    if (mealPlan != null) {
        MealPlanScreen1(mealPlan)
    } else {
        Toast.makeText(context, "Failed to fetch meal plan. Please try again.", Toast.LENGTH_SHORT).show()
    }
}

private fun navigateToSexSelection(context: Context) {
    val intent = Intent(context, SexSelectionActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
}
@Composable
fun MealPlanScreen(mealPlan: MealNutrients) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
                mealPlan.week.apply {
                    listOf(monday, tuesday, wednesday, thursday, friday, saturday, sunday).forEach { day ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            day.meals.forEach { meal ->
                                MealCard(meal)
                            }
                            NutrientsCard(day.nutrients)
                        }
                    }
                }
            }
        }
    }
@Composable
fun MealPlanScreen1(mealPlan: Day) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // Display meals section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mealplan),
                    contentDescription = "Meals Background Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    mealPlan.meals.forEach { meal ->
                        MealCard(meal)
                    }
                }
            }

            // Display nutrients section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.weight),
                    contentDescription = "Nutrients Background Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    NutrientsCard(mealPlan.nutrients)
                }
            }
        }
    }
}


@Composable
fun NutrientsCard(nutrients: Nutrients) {
    MaterialTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nutrients", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Calories: ${nutrients.calories}")
                Text(text = "Protein: ${nutrients.protein}")
                Text(text = "Fat: ${nutrients.fat}")
                Text(text = "Carbohydrates: ${nutrients.carbohydrates}")
            }
        }
    }
}

@Composable
fun MealCard(meal: Meal) {
    MaterialTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Meal", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Title: ${meal.title}")
                Text(text = "Ready in minutes: ${meal.readyInMinutes}")
                Text(text = "Servings: ${meal.servings}")
                Text(text = "Source URL: ${meal.sourceUrl}")
            }
        }
    }
}