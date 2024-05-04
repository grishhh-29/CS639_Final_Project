package com.example.finalproject.headspace

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalproject.R
import com.example.finalproject.dietplan.BottomMenu
import com.example.finalproject.dietplan.BottomMenuContent
import com.example.finalproject.dietplan.SuccessActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

class ExerciseActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WorkoutScreen()
        }
    }
    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        val context = this
        navigateToMyFitnessActivity(context)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkoutScreen() {
    val context = LocalContext.current
    val exercises = remember {
        listOf(
            Exercise("Jumping Jacks", R.drawable.ic_jumping_jacks),
            Exercise("High Knees running", R.drawable.ic_high_knees_running_in_place),
            /*Exercise("Body Squat", R.drawable.ic_squat),
            Exercise("Lunges", R.drawable.ic_lunge),
            Exercise("Wall Sit", R.drawable.ic_wall_sit),
            Exercise("Chair Step Up", R.drawable.ic_step_up_onto_chair),
            Exercise("Pushups", R.drawable.ic_push_up),
            Exercise("Triceps Dip", R.drawable.ic_triceps_dip_on_chair),
            Exercise("Pushup & Rotation", R.drawable.ic_push_up_and_rotation),
            Exercise("Side Plank", R.drawable.ic_side_plank),
            Exercise("Abdominal Crunch", R.drawable.ic_abdominal_crunch),
            Exercise("Plank", R.drawable.ic_plank),*/
        )
    }

    var currentExerciseIndex by remember { mutableStateOf(-1) }
    var timeLeft by remember { mutableStateOf(10_000L) } // Initially 10 seconds for the start countdown
    var isExercise by remember { mutableStateOf(true) } // Tracks whether it's exercise time or rest time
    var workoutFinished by remember { mutableStateOf(false) }
    val workoutDates = mutableListOf<WorkoutDate>()

    Box(modifier = Modifier.fillMaxSize()) {
        LaunchedEffect(Unit) {
            // Start countdown for 10 seconds when the screen loads
            while (timeLeft > 0) {
                delay(1000)
                timeLeft -= 1000
            }

            // Start displaying exercises after countdown
            currentExerciseIndex++

            while (currentExerciseIndex < exercises.size) {
                timeLeft = if (isExercise) {
                    60_000L // Set time for exercise (60 secs)
                } else {
                    30_000L // Set time for rest (30 secs)
                }

                // Start countdown for exercise or rest
                while (timeLeft > 0) {
                    delay(1000)
                    timeLeft -= 1000

                    // If the time is up for exercise or rest, switch to the other mode
                    if (timeLeft == 0L) {
                        isExercise = !isExercise
                        if (!isExercise) {
                            currentExerciseIndex++
                        }
                        break
                    }
                }

                // If it's the last exercise, break after rest timer
                if (currentExerciseIndex >= exercises.size) {
                    workoutFinished = true
                    break
                }
            }
        }

        // Column containing timer, progress bar, and congrats message
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display current exercise item
            val currentExercise =
                if (currentExerciseIndex >= 0 && currentExerciseIndex < exercises.size) {
                    exercises[currentExerciseIndex]
                } else {
                    null
                }
            currentExercise?.let { ExerciseItem(exercise = it) }

            // Display timer and progress bar
            if (!workoutFinished) {
                Timer(
                    timeLeft = timeLeft,
                    isExercise = isExercise,
                    exerciseName = currentExercise?.name ?: ""
                )
                ExerciseProgressBar(currentExerciseIndex, exercises.size, isExercise)
            } else {
                // Display congrats message and add workout date
                CongratsMessage()
                val currentDate = LocalDate.now()
                val workoutDateManager = WorkoutDateManager(context) // Pass the appropriate context here
                workoutDateManager.addWorkoutDate(currentDate)
                HeadspaceScore.setScore(HeadspaceScore.getScore() + 10)
                val currentScore = HeadspaceScore.getScore()
                ScoreInSharedPreferences.addScoreToSharedPreferences(context,currentScore)
            }
        }

        // Display bottom menu
        BottomMenu(
            items = listOf(
                BottomMenuContent("Home", R.drawable.ic_home),
                BottomMenuContent("Support", R.drawable.ic_bubble),
                BottomMenuContent("Profile", R.drawable.ic_profile)
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

}

@Composable
fun ExerciseProgressBar(currentIndex: Int, totalExercises: Int, isExercise: Boolean) {
    val progress = if (isExercise) currentIndex + 1 else currentIndex
    val anglePerStep = 360f / totalExercises

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(200.dp)
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val outerRadius = size.minDimension / 2f
            val centerX = size.width / 2f
            val centerY = size.height / 2f

            val startAngle = -90f
            var currentAngle = startAngle

            for (i in 0 until totalExercises) {
                val color = if (i < progress) Color.Green else Color.Gray
                val sweepAngle = if (i < progress) anglePerStep else 0f
                drawArc(
                    color = color,
                    startAngle = currentAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(centerX - outerRadius, centerY - outerRadius),
                    size = Size(outerRadius * 2, outerRadius * 2),
                    style = Stroke(width = 20f)
                )
                currentAngle += anglePerStep
            }
        }
        Text(
            text = "${progress}/${totalExercises}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}



@Composable
fun ExerciseItem(exercise: Exercise) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = exercise.imageRes),
            contentDescription = "Exercise Image",
            modifier = Modifier.size(230.dp)
        )
    }
}

@Composable
fun Timer(timeLeft: Long, isExercise: Boolean, exerciseName: String) {
    val minutes = (timeLeft / 1000) / 60
    val seconds = (timeLeft / 1000) % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isExercise) {
            Text(
                text = "Start $exerciseName",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        } else {
            Text(
                text = "Get ready for next exercise: $exerciseName",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Text(
            text = formattedTime,
            fontSize = 48.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}
@Composable
fun CongratsMessage() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.fifteenmint),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
        )
        Text(
            text = "Congratulations! Workout finished!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
}

data class Exercise(val name: String, val imageRes: Int)
private fun navigateToMyFitnessActivity(context: Context) {
    val intent = Intent(context, MyFitness::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}