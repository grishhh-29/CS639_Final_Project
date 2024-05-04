package com.example.finalproject.headspace

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import com.example.finalproject.R

class HistoryScreen : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("background_colors", Context.MODE_PRIVATE)

        setContent {
            val workoutDateManager = WorkoutDateManager(this) // Pass the appropriate context here
            val workoutDates = workoutDateManager.getWorkoutDates()

            HistoryScreenActivity(workoutDates, sharedPreferences)
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        navigateToMyFitnessActivity(this)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreenActivity(workoutDates: List<LocalDate>, sharedPreferences: SharedPreferences) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Color indicator
        Box(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.End)
        ) {
            ColorIndicator()
        }
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "HISTORY!!",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        // Custom calendar view
        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center) // Center content within Box
        ) {
            CalendarView(
                workoutDates = workoutDates,
                sharedPreferences = sharedPreferences
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    workoutDates: List<LocalDate>,
    sharedPreferences: SharedPreferences
) {
    val currentYear = LocalDate.now().year
    val currentMonth = LocalDate.now().monthValue
    val yearMonth = YearMonth.of(currentYear, currentMonth)

    MonthView(yearMonth = yearMonth, workoutDates = workoutDates, sharedPreferences = sharedPreferences)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthView(
    yearMonth: YearMonth,
    workoutDates: List<LocalDate>,
    sharedPreferences: SharedPreferences
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${yearMonth.month.name} ${yearMonth.year}",
            style = MaterialTheme.typography.bodySmall
        )

        // Weekdays
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Days
        val daysInMonth = yearMonth.lengthOfMonth()
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            (1..daysInMonth).chunked(7).forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    week.forEach { day ->
                        Day(day = day, workoutDates = workoutDates, sharedPreferences = sharedPreferences)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Day(day: Int, workoutDates: List<LocalDate>, sharedPreferences: SharedPreferences) {
    val currentDate = LocalDate.now()
    val currentYear = currentDate.year
    val currentMonth = currentDate.month

    val date = LocalDate.of(currentYear, currentMonth, day)

    // Retrieve the saved background color from SharedPreferences
    val backgroundColor = if (workoutDates.any { it.year == currentYear && it.month == currentMonth && it.dayOfMonth == day }) {
        Color.Green
    } else {
        Color.Gray
    }

    // Save the background color to SharedPreferences
    sharedPreferences.edit().putInt("day_$day", backgroundColor.toArgb()).apply()

    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp)) // Rounded corners
            .padding(4.dp)
            .border(1.dp, Color.Black), // Border
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            color = Color.Black
        )
    }
}

@Composable
fun ColorIndicator() {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.End
    ) {
        Row {
            Box(
                modifier = Modifier
                    .background(Color.Green, CircleShape)
                    .size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Worked Out",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Row {
            Box(
                modifier = Modifier
                    .background(Color.Gray, CircleShape)
                    .size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Not Attended",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun navigateToMyFitnessActivity(context: Context) {
    val intent = Intent(context, MyFitness::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}
