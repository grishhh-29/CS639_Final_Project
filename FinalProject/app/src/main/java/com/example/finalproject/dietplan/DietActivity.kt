package com.example.finalproject.dietplan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import android.net.Uri
import android.media.MediaPlayer
import android.view.SurfaceHolder
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalproject.R
import kotlinx.coroutines.delay

class DietActivity : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        val weight = intent.getDoubleExtra("WEIGHT", 0.0)
        val height = intent.getDoubleExtra("HEIGHT", 0.0)
        super.onCreate(savedInstanceState)
        setContent {
            DietScreen(this,weight,height)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        val context = this
        navigateToHeightScreen(context)
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun DietScreen(context: Context,weight: Double,height:Double) {
    val videoUrl = "https://drive.google.com/uc?export=download&id=1lzgCZrWGII5iPy4tgo0L0FvKJYTzdd4y"
    val videoUri = Uri.parse(videoUrl)
    // Calculate BMI
    val bmi = calculateBMI(weight, height)

    // Determine BMI category
    val bmiCategory = getBMICategory(bmi)

    // Navigate to the next screen based on BMI category
    //navigateToNextScreen(context, bmiCategory)
    val mediaPlayer = remember {
        MediaPlayer().apply {
            setDataSource(context, videoUri)
            setOnPreparedListener { start() }
            isLooping = true
            prepareAsync()
        }
    }
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val surfaceView = android.view.SurfaceView(context).apply {
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
                )
                holder.addCallback(object : SurfaceHolder.Callback {
                    override fun surfaceCreated(holder: SurfaceHolder) {
                        mediaPlayer.setDisplay(holder)
                    }

                    override fun surfaceChanged(
                        holder: SurfaceHolder,
                        format: Int,
                        width: Int,
                        height: Int
                    ) {
                    }

                    override fun surfaceDestroyed(holder: SurfaceHolder) {
                    }
                })
            }
            surfaceView
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
    // Bouncing button composable
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BouncingButton(
            onClick = {
                navigateToNextScreen(context, weight, height)
            },
            modifier = Modifier.padding(16.dp),
            buttonText = "Categorize your meal ->"
        )
    }
}
@Composable
fun BouncingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String
) {
    var bounceState by remember { mutableStateOf(BounceState.RELEASED) }

    val bounceAnim by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = if (bounceState == BounceState.PRESSED) 0.9f else 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.9f at 1000
                1f at 2000
            }
        ), label = ""
    )

    ElevatedButton(
        onClick = {
            onClick()
            bounceState = BounceState.PRESSED
        },
        modifier = modifier
            .graphicsLayer(scaleY = bounceAnim, scaleX = bounceAnim)
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = buttonText,
            fontSize = 20.sp
        )
    }

    // LaunchedEffect to reset bounceState after a short delay
    LaunchedEffect(bounceState) {
        delay(200)
        bounceState = BounceState.RELEASED
    }
}
enum class BounceState { PRESSED, RELEASED }

// Calculate BMI based on user's weight and height
fun calculateBMI(weight: Double, height: Double): Double {
    return weight / (height * height)
}

// Determine BMI category based on BMI value
fun getBMICategory(bmi: Double): String {
    return when {
        bmi < 18.5 -> "Underweight"
        bmi < 25 -> "Normal weight"
        bmi < 30 -> "Overweight"
        else -> "Obese"
    }
}

private fun navigateToNextScreen(context: Context, weight: Double, height: Double) {
    val intent = Intent(context, CategorizeScreen::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra("WEIGHT", weight)
        putExtra("HEIGHT", height)
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}
private fun navigateToHeightScreen(context: Context) {
    val intent = Intent(context, HeightScreen::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    (context as? Activity)?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}